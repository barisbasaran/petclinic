package io.baris.petclinic.dropwizard.pet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a pet
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pet {

    private int id;
    private String name;
    private int age;
    private Species species;
}
