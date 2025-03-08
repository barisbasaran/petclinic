package io.baris.petclinic.springjersey.pet;

import io.baris.petclinic.springjersey.pet.model.CreatePetRequest;
import io.baris.petclinic.springjersey.pet.model.Pet;
import io.baris.petclinic.springjersey.pet.model.UpdatePetRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

import static io.baris.petclinic.springjersey.pet.PetMapper.mapToCreatePet;
import static io.baris.petclinic.springjersey.pet.PetMapper.mapToUpdatePet;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Handles the pet endpoints
 */

@Path("/pets")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Slf4j
@Component
public class PetResource {

    private final PetManager petManager;

    @Autowired
    public PetResource(PetManager petManager) {
        this.petManager = petManager;
    }

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
    @Path("/{petId}")
    @GET
    public Pet getPet(@PathParam("petId") int petId) {
        return petManager
            .getPet(petId)
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
