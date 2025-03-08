package io.baris.petclinic.springjersey.pet.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Represents the request for updating the pet
 */
@Builder
@Value
public class UpdatePetRequest {

    @NotEmpty
    String name;
    @Min(value = 1, message = "From must be greater than zero")
    int age;
    @NotNull
    Species species;
}
