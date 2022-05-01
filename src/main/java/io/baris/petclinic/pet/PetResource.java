package io.baris.petclinic.pet;

import io.baris.petclinic.pet.model.CreatePetRequest;
import io.baris.petclinic.pet.model.Pet;
import io.baris.petclinic.pet.model.UpdatePetRequest;
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

import static io.baris.petclinic.pet.PetMapper.mapToCreatePet;
import static io.baris.petclinic.pet.PetMapper.mapToUpdatePet;

/**
 * Pet resource to serve pet endpoints
 */
@Path("/pets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
@RequiredArgsConstructor
public class PetResource {

    private final PetManager petManager;

    @Operation(
        summary = "Get pet",
        tags = {"Pet"},
        responses = {
            @ApiResponse(
                description = "The pet",
                content = @Content(schema = @Schema(implementation = Pet.class))
            ),
            @ApiResponse(responseCode = "404", description = "Pet not found")
        }
    )
    @GET
    @Path("/{id}")
    public Pet getPet(
        final @PathParam("id") int id
    ) {
        return petManager
            .getPet(id)
            .orElseThrow(() -> new NotFoundException("Pet not found"));
    }

    @Operation(
        summary = "Get all pets",
        tags = {"Pet"},
        responses = {
            @ApiResponse(
                description = "All pets",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = Pet.class)))
            )
        }
    )
    @GET
    public List<Pet> getAllPets() {
        return petManager.getAllPets();
    }

    @Operation(
        summary = "Create pet",
        tags = {"Pet"},
        responses = {
            @ApiResponse(
                description = "The pet",
                content = @Content(schema = @Schema(implementation = Pet.class))
            ),
            @ApiResponse(responseCode = "422", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Pet could not be created")
        }
    )
    @PUT
    public Pet createPet(
        final @Valid CreatePetRequest createPetRequest
    ) {
        return petManager
            .createPet(mapToCreatePet(createPetRequest))
            .orElseThrow(() -> new InternalServerErrorException("Pet could not be created"));
    }

    @Operation(
        summary = "Update pet",
        tags = {"Pet"},
        responses = {
            @ApiResponse(
                description = "The pet",
                content = @Content(schema = @Schema(implementation = Pet.class))
            ),
            @ApiResponse(responseCode = "422", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Pet not found"),
            @ApiResponse(responseCode = "500", description = "Pet could not be updated")
        }
    )
    @Path("{id}")
    @POST
    public Pet updatePet(
        final @PathParam("id") int id,
        final @Valid UpdatePetRequest updatePetRequest
    ) {
        return petManager
            .updatePet(mapToUpdatePet(id, updatePetRequest))
            .orElseThrow(() -> new NotFoundException("Pet not found"));
    }
}
