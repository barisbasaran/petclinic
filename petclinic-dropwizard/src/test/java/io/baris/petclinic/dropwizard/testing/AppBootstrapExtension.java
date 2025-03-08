package io.baris.petclinic.dropwizard.testing;

import io.baris.petclinic.dropwizard.PetclinicApplication;
import io.baris.petclinic.dropwizard.system.PetclinicConfiguration;
import io.baris.petclinic.dropwizard.system.PetClinicUtils;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.junit5.DropwizardAppExtension;

/**
 * Junit rule to start the application
 */
public class AppBootstrapExtension extends DropwizardAppExtension<PetclinicConfiguration> {

    public AppBootstrapExtension(
        final String configPath,
        final String databaseUrl
    ) {
        super(
            PetclinicApplication.class,
            PetClinicUtils.resourceFilePath(configPath),
            ConfigOverride.config("database.url", databaseUrl)
        );
    }
}
