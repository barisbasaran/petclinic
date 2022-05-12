package io.baris.petclinic.dropwizard.system;

import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;

import static java.util.EnumSet.allOf;
import static org.eclipse.jetty.servlets.CrossOriginFilter.ALLOWED_HEADERS_PARAM;
import static org.eclipse.jetty.servlets.CrossOriginFilter.ALLOWED_METHODS_PARAM;
import static org.eclipse.jetty.servlets.CrossOriginFilter.ALLOWED_ORIGINS_PARAM;

public class CorsConfigurer {

    public static void configureCors(final Environment environment) {
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
