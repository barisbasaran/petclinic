package io.baris.petclinic.visit.model;

import io.baris.petclinic.pet.model.Pet;
import io.baris.petclinic.vet.model.Vet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Represents a visit
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visit {

    private int id;
    private Pet pet;
    private Vet vet;
    private Instant date;
    private String treatment;
}
