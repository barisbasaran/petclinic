package io.baris.petclinic.pet;

import io.baris.petclinic.pet.model.Species;
import io.baris.petclinic.pet.model.UpdatePetRequest;
import io.baris.petclinic.testing.AppBootstrapRule;
import io.baris.petclinic.testing.DbCleanupRule;
import io.baris.petclinic.testing.PostgreRule;
import io.baris.petclinic.pet.model.CreatePetRequest;
import io.baris.petclinic.pet.model.Pet;
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
public class PetIntegrationTest {

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
    public void getAllPets() {
        // arrange
        postgre.addPet("Sofi", 2, Species.CAT);
        postgre.addPet("Lucky", 5, Species.DOG);

        // act
        var response = app.client()
            .target(getTargetUrl())
            .path("pets")
            .request()
            .get();

        // assert
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);

        var pets = response.readEntity(Pet[].class);
        assertThat(pets).hasSize(2);

        var lucky = pets[0];
        assertThat(lucky.getName()).isEqualTo("Lucky");
        assertThat(lucky.getAge()).isEqualTo(5);
        assertThat(lucky.getSpecies()).isEqualTo(Species.DOG);

        var sofi = pets[1];
        assertThat(sofi.getName()).isEqualTo("Sofi");
        assertThat(sofi.getAge()).isEqualTo(2);
        assertThat(sofi.getSpecies()).isEqualTo(Species.CAT);
    }

    @Test
    public void getPet_PetFound() {
        // arrange
        postgre.addPet("Sofi", 2, Species.CAT);
        var pet = postgre.getPet("Sofi");
        assertThat(pet).isPresent();

        // act
        var response = app.client()
            .target(getTargetUrl())
            .path("pets")
            .path(String.valueOf(pet.get().getId()))
            .request()
            .get();

        // assert
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        var sofi = response.readEntity(Pet.class);

        assertThat(sofi.getName()).isEqualTo("Sofi");
        assertThat(sofi.getAge()).isEqualTo(2);
        assertThat(sofi.getSpecies()).isEqualTo(Species.CAT);
    }

    @Test
    public void getPet_PetNotFound() {
        // act
        var response = app.client()
            .target(getTargetUrl())
            .path("pets")
            .path("100")
            .request()
            .get();

        // assert
        assertThat(response.getStatusInfo()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void createPet_Success() {
        // act
        var createPetRequest = CreatePetRequest.builder()
            .name("Sofi")
            .age(4)
            .species(Species.CAT)
            .build();
        var sofi = app.client()
            .target(getTargetUrl())
            .path("pets")
            .request()
            .put(Entity.json(createPetRequest), Pet.class);

        // assert
        assertThat(sofi).isNotNull();
        assertThat(sofi.getId()).isNotNull();
        assertThat(sofi.getName()).isEqualTo("Sofi");
        assertThat(sofi.getAge()).isEqualTo(4);
        assertThat(sofi.getSpecies()).isEqualTo(Species.CAT);

        // verify DB changes
        var sofiInDb = postgre.getPet("Sofi");
        assertThat(sofiInDb).isPresent();
        assertThat(sofiInDb.get().getName()).isEqualTo("Sofi");
        assertThat(sofiInDb.get().getAge()).isEqualTo(4);
        assertThat(sofiInDb.get().getSpecies()).isEqualTo(Species.CAT);
    }

    @Test
    public void createPet_FailWhenSameName() {
        // arrange
        postgre.addPet("Sofi", 2, Species.CAT);

        // act
        var createPetRequest = CreatePetRequest.builder()
            .name("Sofi")
            .build();
        var response = app.client()
            .target(getTargetUrl())
            .path("pets")
            .request()
            .put(Entity.json(createPetRequest));

        // assert
        assertThat(response.getStatusInfo()).isEqualTo(INTERNAL_SERVER_ERROR);
    }

    @Test
    public void updatePet_Success() {
        // arrange
        var oldName = "Sofi";
        postgre.addPet(oldName, 2, Species.CAT);

        var petBefore = postgre.getPet(oldName);
        assertThat(petBefore).isPresent();

        // act
        var newName = "Sofi Junior";
        var updatePetRequest = UpdatePetRequest.builder()
            .name(newName)
            .age(6)
            .species(Species.CAT)
            .build();
        var sofi = app.client()
            .target(getTargetUrl())
            .path("pets")
            .path(String.valueOf(petBefore.get().getId()))
            .request()
            .post(Entity.json(updatePetRequest), Pet.class);

        // assert
        assertThat(sofi).isNotNull();
        assertThat(sofi.getId()).isNotNull();
        assertThat(sofi.getName()).isEqualTo(newName);
        assertThat(sofi.getAge()).isEqualTo(6);
        assertThat(sofi.getSpecies()).isEqualTo(Species.CAT);

        // verify DB changes
        assertThat(postgre.getPet(newName)).isPresent();
        assertThat(postgre.getPet(oldName)).isEmpty();
    }

    @Test
    public void updatePet_FailWhenNotFound() {
        // act
        var newName = "Sofi Karl";
        var createPetRequest = CreatePetRequest.builder()
            .name(newName)
            .build();
        var response = app.client()
            .target(getTargetUrl())
            .path("pets")
            .path(String.valueOf(1))
            .request()
            .post(Entity.json(createPetRequest));

        // assert
        assertThat(response.getStatusInfo()).isEqualTo(NOT_FOUND);
    }

    private String getTargetUrl() {
        return "http://localhost:%d".formatted(app.getLocalPort());
    }
}
