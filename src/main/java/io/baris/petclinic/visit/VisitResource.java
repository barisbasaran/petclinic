package io.baris.petclinic.visit;

import io.baris.petclinic.pet.PetManager;
import io.baris.petclinic.vet.VetManager;
import io.baris.petclinic.visit.model.MakeVisitRequest;
import io.baris.petclinic.visit.model.Visit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static io.baris.petclinic.visit.VisitMapper.mapToMakeVisit;

/**
 * Visit resource to serve visit endpoints
 */
@Path("/visits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
@RequiredArgsConstructor
public class VisitResource {

    private final VisitManager visitManager;
    private final PetManager petManager;
    private final VetManager vetManager;

    @Operation(
        summary = "Make visit",
        responses = {
            @ApiResponse(responseCode = "400", description = "Date cannot be in the future"),
            @ApiResponse(responseCode = "400", description = "Pet not found"),
            @ApiResponse(responseCode = "400", description = "Vet not found"),
            @ApiResponse(responseCode = "500", description = "Visit could not be created")
        }
    )
    @PUT
    @Path("/pets/{petId}/vets/{vetId}")
    public void makeVisit(
        final @PathParam("petId") int petId,
        final @PathParam("vetId") int vetId,
        final @Valid MakeVisitRequest createPetRequest
    ) {
        // validation
        petManager.getPet(petId)
            .orElseThrow(() -> new BadRequestException("Pet does not exist"));
        vetManager.getVet(vetId)
            .orElseThrow(() -> new BadRequestException("Pet does not exist"));

        visitManager.makeVisit(mapToMakeVisit(petId, vetId, createPetRequest));
    }

    @Operation(
        summary = "Gets visits for a pet",
        responses = {
            @ApiResponse(
                description = "Visits for a pet",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = Visit.class)))
            )
        }
    )
    @GET
    @Path("/pets/{petId}")
    public List<Visit> getPetVisits(
        final @PathParam("petId") int petId
    ) {
        // validation
        petManager.getPet(petId)
            .orElseThrow(() -> new BadRequestException("Pet does not exist"));

        return visitManager.getPetVisits(petId);
    }
}
