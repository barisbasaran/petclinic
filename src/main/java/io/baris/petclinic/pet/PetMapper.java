package io.baris.petclinic.pet;

import io.baris.petclinic.pet.model.CreatePet;
import io.baris.petclinic.pet.model.CreatePetRequest;
import io.baris.petclinic.pet.model.UpdatePet;
import io.baris.petclinic.pet.model.UpdatePetRequest;

/**
 * Maps pet api classes
 */
public class PetMapper {

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
}
