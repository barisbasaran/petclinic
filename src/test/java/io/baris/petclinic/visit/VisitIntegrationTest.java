package io.baris.petclinic.visit;

import io.baris.petclinic.pet.model.Species;
import io.baris.petclinic.testing.AppBootstrapRule;
import io.baris.petclinic.testing.DbCleanupRule;
import io.baris.petclinic.testing.PostgreRule;
import io.baris.petclinic.visit.model.MakeVisitRequest;
import io.baris.petclinic.visit.model.Visit;
import lombok.extern.slf4j.Slf4j;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class VisitIntegrationTest {

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

        // verify DB changes
        var visitsInDb = postgre.getPetVisits(sofi.get().getId());
        assertThat(visitsInDb).hasSize(1);

        var visitInDb = visitsInDb.get(0);
        assertThat(visitInDb.getDate()).isEqualTo(makeVisitRequest.getDate());
        assertThat(visitInDb.getTreatment()).isEqualTo("flu");
        assertThat(visitInDb.getVetId()).isEqualTo(magnus.get().getId());
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
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);

        var visits = response.readEntity(Visit[].class);
        assertThat(visits).hasSize(2);

        var visit1 = visits[0];
        assertThat(visit1.getPet()).isEqualTo(sofi.get());
        assertThat(visit1.getVet()).isEqualTo(magnus.get());
        assertThat(visit1.getDate()).isEqualTo(dateFirstVisit);
        assertThat(visit1.getTreatment()).isEqualTo("flu");

        var visit2 = visits[1];
        assertThat(visit2.getPet()).isEqualTo(sofi.get());
        assertThat(visit2.getVet()).isEqualTo(erica.get());
        assertThat(visit2.getDate()).isEqualTo(dateSecondVisit);
        assertThat(visit2.getTreatment()).isEqualTo("parasites");
    }

    private String getTargetUrl() {
        return "http://localhost:%d".formatted(app.getLocalPort());
    }
}
