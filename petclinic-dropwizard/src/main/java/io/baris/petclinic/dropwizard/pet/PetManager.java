package io.baris.petclinic.dropwizard.pet;

import io.baris.petclinic.dropwizard.pet.model.Pet;
import io.baris.petclinic.dropwizard.pet.model.CreatePet;
import io.baris.petclinic.dropwizard.pet.model.UpdatePet;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Optional;

/**
 * Manages the pet
 */
@RequiredArgsConstructor
public class PetManager {

    private final Jdbi jdbi;

    public Optional<Pet> getPet(final int id) {
        return jdbi.withExtension(PetDao.class, dao -> dao.getPet(id));
    }

    public Optional<Pet> getPet(final String name) {
        return jdbi.withExtension(PetDao.class, dao -> dao.getPet(name));
    }

    public List<Pet> getAllPets() {
        return jdbi.withExtension(PetDao.class, PetDao::getAllPets);
    }

    public Optional<Pet> createPet(final CreatePet createPet) {
        return jdbi.withExtension(PetDao.class, dao -> dao.createPet(createPet));
    }

    public Optional<Pet> updatePet(final UpdatePet updatePet) {
        return jdbi.withExtension(PetDao.class, dao -> dao.updatePet(updatePet));
    }
}
