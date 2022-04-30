package io.baris.petclinic.pet.model;

import lombok.Builder;
import lombok.Value;

/**
 * Represents the request for creating a new pet
 */
@Builder
@Value
public class CreatePetRequest {

    String name;
    int age;
    Species species;
}
