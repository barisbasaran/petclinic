package io.baris.petclinic.vet;

import io.baris.petclinic.testing.AppBootstrapRule;
import io.baris.petclinic.testing.DbCleanupRule;
import io.baris.petclinic.testing.PostgreRule;
import io.baris.petclinic.vet.model.CreateVetRequest;
import io.baris.petclinic.vet.model.Vet;
import lombok.extern.slf4j.Slf4j;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class VetIntegrationTest {

    public static final String TEST_CONFIG = "test-config.yml";

    @ClassRule(order = 0)
    public static PostgreRule postgre = new PostgreRule(TEST_CONFIG);

    @ClassRule(order = 1)
    public static AppBootstrapRule app = new AppBootstrapRule(
        TEST_CONFIG,
        postgre.getContainer().getJdbcUrl()
    );

    @Rule
    public DbCleanupRule dbCleanupRule = new DbCleanupRule(postgre.getJdbi());

    @Test
    public void getAllVets() {
        // arrange
        postgre.addVet("Magnus");
        postgre.addVet("Erica");

        // act
        var response = app.client()
            .target(getTargetUrl())
            .path("vets")
            .request()
            .get();

        // assert
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        var vets = response.readEntity(Vet[].class);

        assertThat(vets).hasSize(2);
        assertThat(vets[0].getName()).isEqualTo("Erica");
        assertThat(vets[1].getName()).isEqualTo("Magnus");
    }

    @Test
    public void getVet_VetFound() {
        // arrange
        postgre.addVet("Magnus");
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
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        var vet = response.readEntity(Vet.class);

        assertThat(vet.getName()).isEqualTo("Magnus");
    }

    @Test
    public void getVet_VetNotFound() {
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

        // verify DB changes
        var vetInDb = postgre.getVet("Magnus");
        assertThat(vetInDb).isPresent();
        assertThat(vetInDb.get().getName()).isEqualTo("Magnus");
    }

    @Test
    public void createVet_FailWhenSameName() {
        // arrange
        postgre.addVet("Magnus");

        // act
        var createVetRequest = CreateVetRequest.builder()
            .name("Magnus")
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
    public void updateVet_Success() {
        // arrange
        var oldName = "Magnus";
        postgre.addVet(oldName);

        var vetBefore = postgre.getVet(oldName);
        assertThat(vetBefore).isPresent();

        // act
        var newName = "Magnus Karl";
        var createVetRequest = CreateVetRequest.builder()
            .name(newName)
            .build();
        var vet = app.client()
            .target(getTargetUrl())
            .path("vets")
            .path(String.valueOf(vetBefore.get().getId()))
            .request()
            .post(Entity.json(createVetRequest), Vet.class);

        // assert
        assertThat(vet).isNotNull();
        assertThat(vet.getId()).isNotNull();
        assertThat(vet.getName()).isEqualTo(newName);

        // verify DB changes
        assertThat(postgre.getVet(newName)).isPresent();
        assertThat(postgre.getVet(oldName)).isEmpty();
    }

    @Test
    public void updateVet_FailWhenNotFound() {
        // act
        var newName = "Magnus Karl";
        var createVetRequest = CreateVetRequest.builder()
            .name(newName)
            .build();
        var response = app.client()
            .target(getTargetUrl())
            .path("vets")
            .path(String.valueOf(1))
            .request()
            .post(Entity.json(createVetRequest));

        // assert
        assertThat(response.getStatusInfo()).isEqualTo(NOT_FOUND);
    }

    private String getTargetUrl() {
        return "http://localhost:%d".formatted(app.getLocalPort());
    }
}
