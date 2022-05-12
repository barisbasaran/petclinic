package io.baris.petclinic.dropwizard;

import io.baris.petclinic.dropwizard.homepage.HomepageResource;
import io.baris.petclinic.dropwizard.pet.PetManager;
import io.baris.petclinic.dropwizard.pet.PetResource;
import io.baris.petclinic.dropwizard.system.PetclinicConfiguration;
import io.baris.petclinic.dropwizard.system.PetclinicHealthCheck;
import io.baris.petclinic.dropwizard.vet.VetManager;
import io.baris.petclinic.dropwizard.vet.VetResource;
import io.baris.petclinic.dropwizard.visit.VisitManager;
import io.baris.petclinic.dropwizard.visit.VisitResource;
import io.dropwizard.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import static io.baris.petclinic.dropwizard.system.CorsConfigurer.configureCors;
import static io.baris.petclinic.dropwizard.system.PostgreUtils.applySqlScript;

/**
 * Vet service application class to bootstrap the application
 */
public class PetclinicApplication extends Application<PetclinicConfiguration> {

    public static void main(final String[] args) throws Exception {
        new PetclinicApplication().run(args);
    }

    @Override
    public String getName() {
        return "Pet Clinic";
    }

    @Override
    public void initialize(final Bootstrap<PetclinicConfiguration> bootstrap) {
    }

    @Override
    public void run(
        final PetclinicConfiguration configuration,
        final Environment environment
    ) {
        environment.healthChecks().register("health", new PetclinicHealthCheck());

        initialiseBeans(configuration, environment);

        configureCors(environment);
    }

    private void initialiseBeans(
        final PetclinicConfiguration configuration,
        final Environment environment
    ) {
        var jdbi = new JdbiFactory()
            .build(environment, configuration.getDatabase(), configuration.getDatabaseConfig().getName());
        jdbi.installPlugin(new SqlObjectPlugin());

        // initialize DB schema
        applySqlScript(jdbi, configuration.getDatabaseConfig().getInitScript());

        var vetManager = new VetManager(jdbi);
        var petManager = new PetManager(jdbi);
        var visitManager = new VisitManager(jdbi);

        // register resources
        environment.jersey().register(new VetResource(vetManager));
        environment.jersey().register(new PetResource(petManager));
        environment.jersey().register(new VisitResource(visitManager, petManager, vetManager));
        environment.jersey().register(new HomepageResource());
        environment.jersey().register(new OpenApiResource());
    }
}
