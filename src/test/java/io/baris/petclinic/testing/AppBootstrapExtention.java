package io.baris.petclinic.testing;

import io.baris.petclinic.PetclinicApplication;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;

/**
 * Junit rule to start the application
 */
public class AppBootstrapExtention extends DropwizardAppExtension {

    public AppBootstrapExtention(
        final String configPath,
        final String databaseUrl
    ) {
        super(
            PetclinicApplication.class,
            ResourceHelpers.resourceFilePath(configPath),
            ConfigOverride.config("database.url", databaseUrl)
        );
    }
}
