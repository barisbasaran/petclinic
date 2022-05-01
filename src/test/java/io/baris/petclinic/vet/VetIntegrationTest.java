package io.baris.petclinic.vet;

import io.baris.petclinic.testing.AppBootstrapExtension;
import io.baris.petclinic.testing.DbResetExtension;
import io.baris.petclinic.testing.PostgreExtension;
import io.baris.petclinic.vet.model.CreateVetRequest;
import io.baris.petclinic.vet.model.UpdateVetRequest;
import io.baris.petclinic.vet.model.Vet;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.ws.rs.client.Entity;
import java.util.Set;

import static io.baris.petclinic.testing.TestUtils.TEST_CONFIG;
import static io.baris.petclinic.testing.TestUtils.UNPROCESSIBLE_ENTITY;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class VetIntegrationTest {

    @RegisterExtension
    @Order(0)
    public static PostgreExtension postgre = new PostgreExtension(TEST_CONFIG);

    @RegisterExtension
    @Order(1)
    public static AppBootstrapExtension app = new AppBootstrapExtension(TEST_CONFIG, postgre.getDatabaseUrl());

    @RegisterExtension
    public DbResetExtension dbReset = new DbResetExtension(postgre.getJdbi());

    @Test
    public void getAllVets_Success() {
        // arrange
        postgre.addVet("Magnus", "radiology", "dentistry");
        postgre.addVet("Erica", "surgery");

        // act
        var response = app.client()
            .target(getTargetUrl())
            .path("vets")
            .request()
            .get();

        // assert
        assertThat(response.getStatusInfo()).isEqualTo(OK);
        var vets = response.readEntity(Vet[].class);

        assertThat(vets).hasSize(2);
        assertThat(vets[0].getName()).isEqualTo("Erica");
        assertThat(vets[0].getSpecialties()).isEqualTo(Set.of("surgery"));
        assertThat(vets[1].getName()).isEqualTo("Magnus");
        assertThat(vets[1].getSpecialties()).isEqualTo(Set.of("radiology", "dentistry"));
    }

    @Test
    public void getVet_Success() {
        // arrange
        postgre.addVet("Magnus", "radiology", "dentistry");
        var magnus = postgre.getVet("Magnus");
        assertThat(magnus).isPresent();

        // act
        var response = app.client()
            .target(getTargetUrl())
            .path("vets")
            .path(String.valueOf(magnus.get().getId()))
            .request()
            .get();

        // assert
        assertThat(response.getStatusInfo()).isEqualTo(OK);
        var vet = response.readEntity(Vet.class);

        assertThat(vet.getName()).isEqualTo("Magnus");
        assertThat(vet.getSpecialties()).isEqualTo(Set.of("radiology", "dentistry"));
    }

    @Test
    public void getVet_FailWhenNotFound() {
        // act
        var response = app.client()
            .target(getTargetUrl())
            .path("vets")
            .path("100")
            .request()
            .get();

        // assert
        assertThat(response.getStatusInfo()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void createVet_Success() {
        // act
        var createVetRequest = CreateVetRequest.builder()
            .name("Magnus")
            .specialties(Set.of("radiology", "dentistry"))
            .build();
        var vet = app.client()
            .target(getTargetUrl())
            .path("vets")
            .request()
            .put(Entity.json(createVetRequest), Vet.class);

        // assert
        assertThat(vet).isNotNull();
        assertThat(vet.getId()).isNotNull();
        assertThat(vet.getName()).isEqualTo("Magnus");
        assertThat(vet.getSpecialties())
            .isEqualTo(Set.of("radiology", "dentistry"));

        // verify DB changes
        var vetInDb = postgre.getVet("Magnus");
        assertThat(vetInDb).isPresent();
        assertThat(vetInDb.get().getName()).isEqualTo("Magnus");
        assertThat(vetInDb.get().getSpecialties())
            .isEqualTo(Set.of("radiology", "dentistry"));
    }

    @Test
    public void createVet_FailWhenSameName() {
        // arrange
        postgre.addVet("Magnus");

        // act
        var createVetRequest = CreateVetRequest.builder()
            .name("Magnus")
            .specialties(Set.of("surgery"))
            .build();
        var response = app.client()
            .target(getTargetUrl())
            .path("vets")
            .request()
            .put(Entity.json(createVetRequest));

        // assert
        assertThat(response.getStatusInfo()).isEqualTo(INTERNAL_SERVER_ERROR);
    }

    @Test
    public void createVet_FailWhenMissingName() {
        // act
        var createVetRequest = CreateVetRequest.builder()
            .specialties(Set.of("surgery"))
            .build();
        var response = app.client()
            .target(getTargetUrl())
            .path("vets")
            .request()
            .put(Entity.json(createVetRequest));

        // assert
        assertThat(response.getStatusInfo().getStatusCode())
            .isEqualTo(UNPROCESSIBLE_ENTITY);
    }

    @Test
    public void createVet_FailWhenMissingSpecialties() {
        // act
        var createVetRequest = CreateVetRequest.builder()
            .name("Magnus")
            .specialties(Set.of())
            .build();
        var response = app.client()
            .target(getTargetUrl())
            .path("vets")
            .request()
            .put(Entity.json(createVetRequest));

        // assert
        assertThat(response.getStatusInfo().getStatusCode())
            .isEqualTo(UNPROCESSIBLE_ENTITY);
    }

    @Test
    public void updateVet_Success() {
        // arrange
        var oldName = "Magnus";
        postgre.addVet(oldName, "radiology", "dentistry");

        var vetBefore = postgre.getVet(oldName);
        assertThat(vetBefore).isPresent();
        var vetId = vetBefore.get().getId();

        // act
        var newName = "Magnus Karl";
        var updateVetRequest = UpdateVetRequest.builder()
            .name(newName)
            .specialties(Set.of("surgery"))
            .build();
        var vet = app.client()
            .target(getTargetUrl())
            .path("vets")
            .path(String.valueOf(vetId))
            .request()
            .post(Entity.json(updateVetRequest), Vet.class);

        // assert
        assertThat(vet).isNotNull();
        assertThat(vet.getId()).isNotNull();
        assertThat(vet.getName()).isEqualTo(newName);
        assertThat(vet.getSpecialties()).isEqualTo(Set.of("surgery"));

        // verify DB changes
        var vetInDb = postgre.getVet(vetId);
        assertThat(vetInDb).isPresent();
        assertThat(vetInDb.get().getName()).isEqualTo(newName);
        assertThat(vetInDb.get().getSpecialties()).isEqualTo(Set.of("surgery"));
    }

    @Test
    public void updateVet_FailWhenNotFound() {
        // act
        var updateVetRequest = UpdateVetRequest.builder()
            .name("Magnus")
            .specialties(Set.of("surgery"))
            .build();
        var response = app.client()
            .target(getTargetUrl())
            .path("vets")
            .path(String.valueOf(1))
            .request()
            .post(Entity.json(updateVetRequest));

        // assert
        assertThat(response.getStatusInfo()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void updateVet_FailWhenMissingName() {
        // act
        var updateVetRequest = UpdateVetRequest.builder()
            .specialties(Set.of("surgery"))
            .build();
        var response = app.client()
            .target(getTargetUrl())
            .path("vets")
            .path(String.valueOf(1))
            .request()
            .post(Entity.json(updateVetRequest));

        // assert
        assertThat(response.getStatusInfo().getStatusCode())
            .isEqualTo(UNPROCESSIBLE_ENTITY);
    }

    @Test
    public void updateVet_FailWhenMissingSpecialties() {
        // act
        var updateVetRequest = UpdateVetRequest.builder()
            .name("Magnus")
            .specialties(Set.of())
            .build();
        var response = app.client()
            .target(getTargetUrl())
            .path("vets")
            .path(String.valueOf(1))
            .request()
            .post(Entity.json(updateVetRequest));

        // assert
        assertThat(response.getStatusInfo().getStatusCode())
            .isEqualTo(UNPROCESSIBLE_ENTITY);
    }

    private String getTargetUrl() {
        return "http://localhost:%d".formatted(app.getLocalPort());
    }
}
