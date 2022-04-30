package io.baris.petclinic.vet.model;

import lombok.Builder;
import lombok.Value;

import java.util.Set;

/**
 * Represents the request for updating the vet
 */
@Builder
@Value
public class UpdateVetRequest {

    String name;
    Set<String> specialties;
}
