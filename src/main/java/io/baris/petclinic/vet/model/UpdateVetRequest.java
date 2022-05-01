package io.baris.petclinic.vet.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Represents the request for updating the vet
 */
@Builder
@Value
public class UpdateVetRequest {

    @NotNull
    String name;
    @NotEmpty
    Set<String> specialties;
}
