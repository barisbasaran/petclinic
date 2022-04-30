package io.baris.petclinic;

import io.baris.petclinic.pet.PetDao;
import io.baris.petclinic.pet.PetManager;
import io.baris.petclinic.pet.PetResource;
import io.baris.petclinic.system.PetclinicHealthCheck;
import io.baris.petclinic.vet.VetDao;
import io.baris.petclinic.vet.VetManager;
import io.baris.petclinic.vet.VetResource;
import io.baris.petclinic.visit.VisitDao;
import io.baris.petclinic.visit.VisitManager;
import io.baris.petclinic.visit.VisitResource;
import io.dropwizard.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;

import static java.util.EnumSet.allOf;
import static org.eclipse.jetty.servlets.CrossOriginFilter.*;

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

        cors(environment);
    }

    private void initialiseBeans(
        final PetclinicConfiguration configuration,
        final Environment environment
    ) {
        var jdbi = new JdbiFactory()
            .build(environment, configuration.getDatabase(), "mydb");

        var vetDao = new VetDao(jdbi);
        var vetManager = new VetManager(vetDao);

        var petDao = new PetDao(jdbi);
        var petManager = new PetManager(petDao);

        var visitDao = new VisitDao(jdbi);
        var visitManager = new VisitManager(visitDao);

        // register resources
        environment.jersey().register(new VetResource(vetManager));
        environment.jersey().register(new PetResource(petManager));
        environment.jersey().register(new VisitResource(visitManager, petManager, vetManager));
        environment.jersey().register(new OpenApiResource());
    }

    private void cors(final Environment environment) {
        // CORS headers
        var cors = environment.servlets()
            .addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter(ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(ALLOWED_HEADERS_PARAM, "Authorization,X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter(ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(allOf(DispatcherType.class), true, "/*");
    }
}
