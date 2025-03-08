package io.baris.petclinic.springjersey.pet;

import io.baris.petclinic.springjersey.pet.model.CreatePet;
import io.baris.petclinic.springjersey.pet.model.CreatePetRequest;
import io.baris.petclinic.springjersey.pet.model.Pet;
import io.baris.petclinic.springjersey.pet.model.PetEntity;
import io.baris.petclinic.springjersey.pet.model.UpdatePet;
import io.baris.petclinic.springjersey.pet.model.UpdatePetRequest;

/**
 * Maps pet model classes
 */
public class PetMapper {

    public static Pet mapToPet(
        final PetEntity petEntity
    ) {
        return Pet.builder()
            .id(petEntity.getId())
            .age(petEntity.getAge())
            .species(petEntity.getSpecies())
            .name(petEntity.getName())
            .build();
    }

    public static UpdatePet mapToUpdatePet(
        final int id,
        final UpdatePetRequest updatePetRequest
    ) {
        return UpdatePet.builder()
            .id(id)
            .age(updatePetRequest.getAge())
            .species(updatePetRequest.getSpecies())
            .name(updatePetRequest.getName())
            .build();
    }

    public static CreatePet mapToCreatePet(
        final CreatePetRequest createPetRequest
    ) {
        return CreatePet.builder()
            .name(createPetRequest.getName())
            .age(createPetRequest.getAge())
            .species(createPetRequest.getSpecies())
            .build();
    }

    public static PetEntity mapToPetEntity(final CreatePet createPet) {
        return PetEntity.builder()
            .name(createPet.getName())
            .age(createPet.getAge())
            .species(createPet.getSpecies())
            .build();
    }

    public static PetEntity mapToPetEntity(final UpdatePet updatePet) {
        return PetEntity.builder()
            .id(updatePet.getId())
            .name(updatePet.getName())
            .age(updatePet.getAge())
            .species(updatePet.getSpecies())
            .build();
    }
}
