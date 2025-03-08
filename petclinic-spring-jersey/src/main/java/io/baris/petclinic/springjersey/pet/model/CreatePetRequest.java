package io.baris.petclinic.springjersey.pet.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Represents the request for creating a new pet
 */
@Builder
@Value
public class CreatePetRequest {

    @NotEmpty
    String name;
    @Min(value = 1, message = "From must be greater than zero")
    int age;
    @NotNull
    Species species;
}
