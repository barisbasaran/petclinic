package io.baris.petclinic.springjersey.system;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ServletContextRequestLoggingFilter;

/**
 * Configures the application
 */
@Configuration
public class PetclinicApplicationConfig {

    /**
     * Logs the HTTP calls
     */
    @Bean
    public ServletContextRequestLoggingFilter logFilter() {
        var filter = new ServletContextRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("REQUEST DATA : ");
        return filter;
    }
}
