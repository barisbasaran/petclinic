package io.baris.petclinic.dropwizard.system;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Vet service configuration
 */
@Data
public class PetclinicConfiguration extends Configuration {

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @NotNull
    private DatabaseConfig databaseConfig;
}
