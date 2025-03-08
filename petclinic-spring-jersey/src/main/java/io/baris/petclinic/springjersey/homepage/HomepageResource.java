package io.baris.petclinic.springjersey.homepage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Handles the homepage endpoints
 */
@Path("/")
public class HomepageResource {

    @Operation(
        summary = "Homepage",
        tags = {"Homepage"},
        responses = {
            @ApiResponse(
                description = "Homepage",
                content = @Content(schema = @Schema(implementation = String.class))
            )
        }
    )
    @GET
    public String homepage() {
        return "Welcome to Pet Clinic";
    }
}
