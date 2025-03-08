package io.baris.petclinic.dropwizard.vet.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Represents the request for creating a new vet
 */
@Builder
@Value
public class CreateVetRequest {

    @NotNull
    String name;
    @NotEmpty
    Set<String> specialties;
}
