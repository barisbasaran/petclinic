package io.baris.petclinic.visit.model;

import lombok.*;

import java.time.Instant;

/**
 * Model for making a visit
 */
@Builder
@Value
public class MakeVisit {

    int petId;
    int vetId;
    Instant date;
    String treatment;
}
