package io.baris.petclinic.visit.model;

import lombok.*;

import java.time.Instant;

/**
 * Model for making a visit
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MakeVisit {

    private int petId;
    private int vetId;
    private Instant date;
    private String treatment;
}
