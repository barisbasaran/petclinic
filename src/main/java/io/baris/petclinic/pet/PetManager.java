package io.baris.petclinic.pet;

import io.baris.petclinic.pet.model.CreatePet;
import io.baris.petclinic.pet.model.Pet;
import io.baris.petclinic.pet.model.UpdatePet;
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
        var pet = jdbi.withExtension(PetDao.class, dao -> dao.getPet(id));
        return Optional.ofNullable(pet);
    }

    public Optional<Pet> getPet(final String name) {
        var pet = jdbi.withExtension(PetDao.class, dao -> dao.getPet(name));
        return Optional.ofNullable(pet);
    }

    public List<Pet> getAllPets() {
        return jdbi.withExtension(PetDao.class, PetDao::getAllPets);
    }

    public Optional<Pet> createPet(
        final CreatePet createPet
    ) {
        var petId = jdbi.withExtension(PetDao.class, dao -> dao.createPet(
            createPet.getName(),
            createPet.getAge(),
            createPet.getSpecies()
        ));
        return getPet(petId);
    }

    public Optional<Pet> updatePet(
        final UpdatePet updatePet
    ) {
        jdbi.withExtension(PetDao.class, dao -> dao.updatePet(
            updatePet.getName(),
            updatePet.getAge(),
            updatePet.getSpecies(),
            updatePet.getId()
        ));
        return getPet(updatePet.getId());
    }
}
