package io.baris.petclinic.dropwizard.visit;

import io.baris.petclinic.dropwizard.pet.model.Species;
import io.baris.petclinic.dropwizard.testing.AppBootstrapExtension;
import io.baris.petclinic.dropwizard.testing.DbResetExtension;
import io.baris.petclinic.dropwizard.testing.PostgreExtension;
import io.baris.petclinic.dropwizard.testing.TestUtils;
import io.baris.petclinic.dropwizard.visit.model.MakeVisitRequest;
import io.baris.petclinic.dropwizard.visit.model.Visit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.ws.rs.client.Entity;
import java.time.Instant;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class VisitIntegrationTest {

    @RegisterExtension
    @Order(0)
    public final static PostgreExtension postgre = new PostgreExtension(TestUtils.TEST_CONFIG);

    @RegisterExtension
    @Order(1)
    public final static AppBootstrapExtension app = new AppBootstrapExtension(TestUtils.TEST_CONFIG, postgre.getDatabaseUrl());

    @RegisterExtension
    public DbResetExtension dbReset = new DbResetExtension(postgre.getJdbi());

    @Test
    public void makeVisit_Success() {
        // arrange
        postgre.addVet("Magnus");
        var magnus = postgre.getVet("Magnus");
        assertThat(magnus).isPresent();

        postgre.addPet("Sofi", 2, Species.CAT);
        var sofi = postgre.getPet("Sofi");
        assertThat(sofi).isPresent();

        // act
        var makeVisitRequest = MakeVisitRequest.builder()
            .date(Instant.parse("2018-11-30T18:35:24.00Z"))
            .treatment("flu")
            .build();
        var visit = app.client()
            .target(getTargetUrl())
            .path("visits")
            .path("pets")
            .path(String.valueOf(sofi.get().getId()))
            .path("vets")
            .path(String.valueOf(magnus.get().getId()))
            .request()
            .put(Entity.json(makeVisitRequest), Visit.class);

        // assert
        assertThat(visit).isNotNull();
        assertThat(visit.getId()).isGreaterThan(0);
        assertThat(visit.getDate()).isEqualTo(makeVisitRequest.getDate());
        assertThat(visit.getTreatment()).isEqualTo("flu");
        assertThat(visit.getPetId()).isEqualTo(sofi.get().getId());
        assertThat(visit.getVetId()).isEqualTo(magnus.get().getId());

        // verify DB changes
        var visitsInDb = postgre.getPetVisits(sofi.get().getId());
        assertThat(visitsInDb).hasSize(1);

        var visitInDb = visitsInDb.get(0);
        assertThat(visitInDb.getDate()).isEqualTo(makeVisitRequest.getDate());
        assertThat(visitInDb.getTreatment()).isEqualTo("flu");
        assertThat(visitInDb.getVetId()).isEqualTo(magnus.get().getId());
    }

    @Test
    public void makeVisit_FailWhenPetDoesNotExist() {
        // act
        var makeVisitRequest = MakeVisitRequest.builder()
            .date(Instant.parse("2018-11-30T18:35:24.00Z"))
            .treatment("flu")
            .build();
        var response = app.client()
            .target(getTargetUrl())
            .path("visits")
            .path("pets")
            .path("1")
            .path("vets")
            .path("1")
            .request()
            .put(Entity.json(makeVisitRequest));

        // assert
        assertThat(response.getStatusInfo()).isEqualTo(BAD_REQUEST);
    }

    @Test
    public void makeVisit_FailWhenVetDoesNotExist() {
        // arrange
        postgre.addPet("Sofi", 2, Species.CAT);
        var sofi = postgre.getPet("Sofi");
        assertThat(sofi).isPresent();

        // act
        var makeVisitRequest = MakeVisitRequest.builder()
            .date(Instant.parse("2018-11-30T18:35:24.00Z"))
            .treatment("flu")
            .build();
        var response = app.client()
            .target(getTargetUrl())
            .path("visits")
            .path("pets")
            .path(String.valueOf(sofi.get().getId()))
            .path("vets")
            .path("1")
            .request()
            .put(Entity.json(makeVisitRequest));

        // assert
        assertThat(response.getStatusInfo()).isEqualTo(BAD_REQUEST);
    }

    @Test
    public void makeVisit_FailWhenMissingDate() {
        // arrange
        postgre.addVet("Magnus");
        var magnus = postgre.getVet("Magnus");
        assertThat(magnus).isPresent();

        postgre.addPet("Sofi", 2, Species.CAT);
        var sofi = postgre.getPet("Sofi");
        assertThat(sofi).isPresent();

        // act
        var makeVisitRequest = MakeVisitRequest.builder()
            .treatment("flu")
            .build();
        var response = app.client()
            .target(getTargetUrl())
            .path("visits")
            .path("pets")
            .path(String.valueOf(sofi.get().getId()))
            .path("vets")
            .path(String.valueOf(magnus.get().getId()))
            .request()
            .put(Entity.json(makeVisitRequest));

        // assert
        assertThat(response.getStatusInfo().getStatusCode())
            .isEqualTo(TestUtils.UNPROCESSIBLE_ENTITY);
    }

    @Test
    public void makeVisit_FailWhenMissingTreatment() {
        // arrange
        postgre.addVet("Magnus");
        var magnus = postgre.getVet("Magnus");
        assertThat(magnus).isPresent();

        postgre.addPet("Sofi", 2, Species.CAT);
        var sofi = postgre.getPet("Sofi");
        assertThat(sofi).isPresent();

        // act
        var makeVisitRequest = MakeVisitRequest.builder()
            .date(Instant.parse("2018-11-30T18:35:24.00Z"))
            .build();
        var response = app.client()
            .target(getTargetUrl())
            .path("visits")
            .path("pets")
            .path(String.valueOf(sofi.get().getId()))
            .path("vets")
            .path(String.valueOf(magnus.get().getId()))
            .request()
            .put(Entity.json(makeVisitRequest));

        // assert
        assertThat(response.getStatusInfo().getStatusCode())
            .isEqualTo(TestUtils.UNPROCESSIBLE_ENTITY);
    }

    @Test
    public void getPetVisits() {
        // arrange
        postgre.addVet("Magnus");
        var magnus = postgre.getVet("Magnus");
        assertThat(magnus).isPresent();

        postgre.addVet("Erica");
        var erica = postgre.getVet("Erica");
        assertThat(erica).isPresent();

        postgre.addPet("Sofi", 2, Species.CAT);
        var sofi = postgre.getPet("Sofi");
        assertThat(sofi).isPresent();

        var dateFirstVisit = Instant.parse("2018-11-30T18:35:24.00Z");
        postgre.addPetVisit(sofi.get(), magnus.get(), dateFirstVisit, "flu");
        var dateSecondVisit = Instant.parse("2019-04-15T09:30:00.00Z");
        postgre.addPetVisit(sofi.get(), erica.get(), dateSecondVisit, "parasites");

        // act
        var response = app.client()
            .target(getTargetUrl())
            .path("visits")
            .path("pets")
            .path(String.valueOf(sofi.get().getId()))
            .request()
            .get();

        // assert
        assertThat(response.getStatusInfo()).isEqualTo(OK);

        var visits = response.readEntity(Visit[].class);
        assertThat(visits).hasSize(2);

        var visit1 = visits[0];
        assertThat(visit1.getId()).isGreaterThan(0);
        assertThat(visit1.getPetId()).isEqualTo(sofi.get().getId());
        assertThat(visit1.getVetId()).isEqualTo(magnus.get().getId());
        assertThat(visit1.getDate()).isEqualTo(dateFirstVisit);
        assertThat(visit1.getTreatment()).isEqualTo("flu");

        var visit2 = visits[1];
        assertThat(visit2.getId()).isGreaterThan(0);
        assertThat(visit2.getPetId()).isEqualTo(sofi.get().getId());
        assertThat(visit2.getVetId()).isEqualTo(erica.get().getId());
        assertThat(visit2.getDate()).isEqualTo(dateSecondVisit);
        assertThat(visit2.getTreatment()).isEqualTo("parasites");
    }

    private String getTargetUrl() {
        return "http://localhost:%d".formatted(app.getLocalPort());
    }
}
