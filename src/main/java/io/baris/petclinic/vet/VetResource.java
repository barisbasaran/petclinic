package io.baris.petclinic.vet;

import io.baris.petclinic.vet.model.CreateVetRequest;
import io.baris.petclinic.vet.model.UpdateVetRequest;
import io.baris.petclinic.vet.model.Vet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static io.baris.petclinic.vet.VetMapper.mapToCreateVet;
import static io.baris.petclinic.vet.VetMapper.mapToUpdateVet;

/**
 * Vet resource to serve vet endpoints
 */
@Path("/vets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
@RequiredArgsConstructor
public class VetResource {

    private final VetManager vetManager;

    @Operation(
        summary = "Gets a vet by ID",
        responses = {
            @ApiResponse(
                description = "The vet",
                content = @Content(schema = @Schema(implementation = Vet.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "Vet not found")
        }
    )
    @GET
    @Path("/{id}")
    public Vet getVet(final @PathParam("id") int id) {
        return vetManager
            .getVet(id)
            .orElseThrow(() -> new NotFoundException("Vet not found"));
    }

    @Operation(
        summary = "Gets all vets",
        responses = {
            @ApiResponse(
                description = "All vets",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = Vet.class)))
            )
        }
    )
    @GET
    public List<Vet> getAllVets() {
        return vetManager.getAllVets();
    }

    @Operation(
        summary = "Create vet",
        responses = {
            @ApiResponse(
                description = "The vet",
                content = @Content(schema = @Schema(implementation = Vet.class))
            ),
            @ApiResponse(responseCode = "500", description = "Vet could not be created")
        }
    )
    @PUT
    public Vet createVet(
        final CreateVetRequest createVetRequest
    ) {
        return vetManager
            .createVet(mapToCreateVet(createVetRequest))
            .orElseThrow(() -> new InternalServerErrorException("Vet could not be created"));
    }

    @Operation(
        summary = "Updates vet",
        responses = {
            @ApiResponse(
                description = "The vet",
                content = @Content(schema = @Schema(implementation = Vet.class))
            ),
            @ApiResponse(responseCode = "500", description = "Vet could not be updated")
        }
    )
    @Path("{id}")
    @POST
    public Vet updateVet(
        final @PathParam("id") int id,
        final UpdateVetRequest updateVetRequest
    ) {
        return vetManager
            .updateVet(mapToUpdateVet(id, updateVetRequest))
            .orElseThrow(() -> new NotFoundException("Vet not found"));
    }
}
