package io.baris.petclinic.vet;

import io.baris.petclinic.vet.model.CreateVet;
import io.baris.petclinic.vet.model.UpdateVet;
import io.baris.petclinic.vet.model.CreateVetRequest;
import io.baris.petclinic.vet.model.UpdateVetRequest;

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
            .build();
    }

    public static CreateVet mapToCreateVet(
        final CreateVetRequest createVetRequest
    ) {
        return CreateVet.builder()
            .name(createVetRequest.getName())
            .build();
    }
}
