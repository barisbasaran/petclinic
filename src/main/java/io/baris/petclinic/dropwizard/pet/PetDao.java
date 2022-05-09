package io.baris.petclinic.dropwizard.pet;

import io.baris.petclinic.dropwizard.pet.model.Pet;
import io.baris.petclinic.dropwizard.pet.model.Species;
import io.baris.petclinic.dropwizard.pet.model.CreatePet;
import io.baris.petclinic.dropwizard.pet.model.UpdatePet;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.util.List;
import java.util.Optional;

/**
 * Manages pets in the database
 */
public interface PetDao {

    @SqlQuery("SELECT * FROM pet WHERE id = ?")
    @RegisterBeanMapper(Pet.class)
    Pet getPetBasic(int id);

    @SqlQuery("SELECT * FROM pet WHERE name = ?")
    @RegisterBeanMapper(Pet.class)
    Pet getPetBasic(String name);

    @SqlQuery("SELECT * FROM pet ORDER BY name")
    @RegisterBeanMapper(Pet.class)
    List<Pet> getAllPets();

    @SqlUpdate("INSERT INTO pet (name, age, species) VALUES (?, ?, ?) returning *")
    @GetGeneratedKeys
    int createPet(String name, int age, Species species);

    @SqlUpdate("UPDATE pet SET name = ?,  age = ?,  species = ? WHERE id = ?")
    void updatePet(String name, int age, Species species, int id);

    @Transaction
    default Optional<Pet> getPet(final int id) {
        return Optional.ofNullable(getPetBasic(id));
    }

    @Transaction
    default Optional<Pet> getPet(final String name) {
        return Optional.ofNullable(getPetBasic(name));
    }

    @Transaction
    default Optional<Pet> createPet(final CreatePet createPet) {
        var petId = createPet(
            createPet.getName(),
            createPet.getAge(),
            createPet.getSpecies()
        );
        return getPet(petId);
    }

    @Transaction
    default Optional<Pet> updatePet(final UpdatePet updatePet) {
        updatePet(
            updatePet.getName(),
            updatePet.getAge(),
            updatePet.getSpecies(),
            updatePet.getId()
        );
        return getPet(updatePet.getId());
    }
}
