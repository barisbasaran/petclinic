package io.baris.petclinic.vet.model;

import lombok.Builder;
import lombok.Value;

import java.util.Set;

/**
 * Model for creating a vet
 */

@Builder
@Value
public class CreateVet {

    String name;
    Set<String> specialties;
}
