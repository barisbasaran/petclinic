package io.baris.petclinic.springjersey.pet.model;

import lombok.Builder;
import lombok.Value;

/**
 * Model for updating the pet
 */

@Builder
@Value
public class UpdatePet {

    int id;
    String name;
    int age;
    Species species;
}
