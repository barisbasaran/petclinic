package io.baris.petclinic.visit.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;

/**
 * Represents the request for making a visit
 */
@Builder
@Value
public class MakeVisitRequest {

    @NotNull
    Instant date;
    @NotEmpty
    String treatment;
}
