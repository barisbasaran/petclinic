package io.baris.petclinic.springjersey.system;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

/**
 * CORS filter
 */
public class CorsResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(
        ContainerRequestContext requestContext,
        ContainerResponseContext responseContext
    ) {
        var headers = responseContext.getHeaders();

        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "OPTIONS,HEAD,GET");
        headers.add("Access-Control-Allow-Headers", "Authorization,X-Requested-With,Content-Type,Accept,Origin");
    }
}