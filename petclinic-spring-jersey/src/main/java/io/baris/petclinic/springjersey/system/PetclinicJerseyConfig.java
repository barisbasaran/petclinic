package io.baris.petclinic.springjersey.system;

import io.baris.petclinic.springjersey.homepage.HomepageResource;
import io.baris.petclinic.springjersey.pet.PetResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

/**
 * Registers the REST resources, filters etc.
 */
@Component
public class PetclinicJerseyConfig extends ResourceConfig {

    public PetclinicJerseyConfig() {
        // register filters
        register(CorsResponseFilter.class);

        // register resources
        register(HomepageResource.class);
        register(PetResource.class);
        register(OpenApiResource.class);
    }
}
