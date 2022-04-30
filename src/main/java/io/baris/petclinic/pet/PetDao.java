package io.baris.petclinic.pet;

import io.baris.petclinic.pet.model.Pet;
import io.baris.petclinic.pet.model.Species;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * Manages pet in the database
 */
public interface PetDao {

    @SqlQuery("SELECT * FROM pet WHERE id = ?")
    @RegisterBeanMapper(Pet.class)
    Pet getPet(int id);

    @SqlQuery("SELECT * FROM pet WHERE name = ?")
    @RegisterBeanMapper(Pet.class)
    Pet getPet(String name);

    @SqlQuery("SELECT * FROM pet ORDER BY name")
    @RegisterBeanMapper(Pet.class)
    List<Pet> getAllPets();

    @SqlUpdate("INSERT INTO pet (name, age, species) VALUES (?, ?, ?) returning *")
    @GetGeneratedKeys
    int createPet(String name, int age, Species species);

    @SqlUpdate("UPDATE pet SET name = ?,  age = ?,  species = ? WHERE id = ?")
    int updatePet(String name, int age, Species species, int id);
}
