package io.baris.petclinic.dropwizard.vet.model;

import lombok.Builder;
import lombok.Value;

import java.util.Set;

/**
 * Model for updating the vet
 */

@Builder
@Value
public class UpdateVet {

    int id;
    String name;
    Set<String> specialties;
}
