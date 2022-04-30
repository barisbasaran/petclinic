package io.baris.petclinic.pet;

import io.baris.petclinic.pet.model.CreatePet;
import io.baris.petclinic.pet.model.Pet;
import io.baris.petclinic.pet.model.UpdatePet;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * Manages the pet
 */
@RequiredArgsConstructor
public class PetManager {

    private final PetDao petDao;

    public Optional<Pet> getPet(final int id) {
        return petDao.getPet(id);
    }

    public List<Pet> getAllPets() {
        return petDao.getAllPets();
    }

    public Optional<Pet> createPet(
        final CreatePet createPet
    ) {
        return petDao.createPet(createPet);
    }

    public Optional<Pet> updatePet(
        final UpdatePet updatePet
    ) {
        return petDao.updatePet(updatePet);
    }
}
