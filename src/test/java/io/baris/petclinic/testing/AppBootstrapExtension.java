package io.baris.petclinic.testing;

import io.baris.petclinic.PetclinicApplication;
import io.baris.petclinic.system.PetClinicUtils;
import io.baris.petclinic.system.PetclinicConfiguration;
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
