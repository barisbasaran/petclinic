package io.baris.petclinic.vet.model;

import lombok.Builder;
import lombok.Value;

import java.util.Set;

/**
 * Represents the request for creating a new vet
 */
@Builder
@Value
public class CreateVetRequest {

    String name;
    Set<String> specialties;
}
