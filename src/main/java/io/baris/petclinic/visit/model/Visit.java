package io.baris.petclinic.visit.model;

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
    private int petId;
    private int vetId;
    private Instant date;
    private String treatment;
}
