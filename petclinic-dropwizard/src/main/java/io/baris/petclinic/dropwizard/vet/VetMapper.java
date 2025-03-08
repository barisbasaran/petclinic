package io.baris.petclinic.dropwizard.vet;

import io.baris.petclinic.dropwizard.vet.model.CreateVet;
import io.baris.petclinic.dropwizard.vet.model.UpdateVet;
import io.baris.petclinic.dropwizard.vet.model.CreateVetRequest;
import io.baris.petclinic.dropwizard.vet.model.UpdateVetRequest;

/**
 * Maps vet api classes
 */
public class VetMapper {

    public static UpdateVet mapToUpdateVet(
        final int id,
        final UpdateVetRequest updateVetRequest
    ) {
        return UpdateVet.builder()
            .id(id)
            .name(updateVetRequest.getName())
            .specialties(updateVetRequest.getSpecialties())
            .build();
    }

    public static CreateVet mapToCreateVet(
        final CreateVetRequest createVetRequest
    ) {
        return CreateVet.builder()
            .name(createVetRequest.getName())
            .specialties(createVetRequest.getSpecialties())
            .build();
    }
}
