package io.baris.petclinic.pet.model;

import lombok.Builder;
import lombok.Value;

/**
 * Represents the request for updating the pet
 */
@Builder
@Value
public class UpdatePetRequest {

    String name;
    int age;
    Species species;
}
