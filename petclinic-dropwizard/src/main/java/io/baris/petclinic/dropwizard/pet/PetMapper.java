package io.baris.petclinic.dropwizard.pet;

import io.baris.petclinic.dropwizard.pet.model.UpdatePetRequest;
import io.baris.petclinic.dropwizard.pet.model.CreatePet;
import io.baris.petclinic.dropwizard.pet.model.CreatePetRequest;
import io.baris.petclinic.dropwizard.pet.model.UpdatePet;

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
