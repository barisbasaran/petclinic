package io.baris.petclinic.vet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Represents a vet
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vet {

    private int id;
    private String name;
    private Set<String> specialties;
}
