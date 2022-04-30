package io.baris.petclinic.visit.model;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

/**
 * Represents the request for making a visit
 */
@Builder
@Value
public class MakeVisitRequest {

    Instant date;
    String treatment;
}
