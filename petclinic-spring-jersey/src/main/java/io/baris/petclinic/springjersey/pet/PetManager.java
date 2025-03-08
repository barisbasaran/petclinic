package io.baris.petclinic.springjersey.pet;

import io.baris.petclinic.springjersey.pet.model.CreatePet;
import io.baris.petclinic.springjersey.pet.model.Pet;
import io.baris.petclinic.springjersey.pet.model.UpdatePet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static io.baris.petclinic.springjersey.pet.PetMapper.mapToPetEntity;

/**
 * Manages the pets
 */
@Component
public class PetManager {

    private final PetRepository petRepository;

    @Autowired
    public PetManager(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Optional<Pet> getPet(final int id) {
        return Optional
            .ofNullable(petRepository.findById(id))
            .map(PetMapper::mapToPet);
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll()
            .stream()
            .map(PetMapper::mapToPet)
            .toList();
    }

    public Optional<Pet> createPet(final CreatePet createPet) {
        return Optional
            .of(petRepository.save(mapToPetEntity(createPet)))
            .map(PetMapper::mapToPet);
    }

    public Optional<Pet> updatePet(final UpdatePet updatePet) {
        return Optional
            .of(petRepository.save(mapToPetEntity(updatePet)))
            .map(PetMapper::mapToPet);
    }
}
