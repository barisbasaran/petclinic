package io.baris.petclinic.dropwizard.system;

import com.codahale.metrics.health.HealthCheck;

/**
 * Health check for the application
 */
public class PetclinicHealthCheck extends HealthCheck {

    @Override
    protected Result check() {
        return Result.healthy();
    }
}
