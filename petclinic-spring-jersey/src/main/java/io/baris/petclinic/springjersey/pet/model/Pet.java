package io.baris.petclinic.springjersey.pet.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Pet {
    int id;
    String name;
    int age;
    Species species;
}
