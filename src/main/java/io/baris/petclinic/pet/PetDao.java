package io.baris.petclinic.pet;

import io.baris.petclinic.pet.model.CreatePet;
import io.baris.petclinic.pet.model.Pet;
import io.baris.petclinic.pet.model.UpdatePet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Optional;

/**
 * Manages pet in the database
 */
@Slf4j
@RequiredArgsConstructor
public class PetDao {

    private final Jdbi jdbi;

    public Optional<Pet> getPet(final int id) {
        return getPetByField("id", id);
    }

    public Optional<Pet> getPet(final String name) {
        return getPetByField("name", name);
    }

    public List<Pet> getAllPets() {
        var pets = jdbi.withHandle(handle ->
            handle.createQuery("SELECT * FROM pet ORDER BY name")
                .mapToBean(Pet.class)
                .list()
        );
        log.debug("Retrieved all pets as{}", pets);

        return pets;
    }

    public Optional<Pet> createPet(
        final CreatePet createPet
    ) {
        var name = createPet.getName();
        jdbi.withHandle(handle ->
            handle.execute("""
                    insert into pet 
                    (name, age, species) 
                    values (?, ?, ?);
                    """,
                name, createPet.getAge(), createPet.getSpecies()
            ));
        var pet = getPet(name);
        log.info("Created pet as {}", pet);
        return pet;
    }

    public Optional<Pet> updatePet(
        final UpdatePet updatePet
    ) {
        var name = updatePet.getName();
        jdbi.withHandle(handle ->
            handle.execute("""
                    update pet set 
                    name = ?,  
                    age = ?,  
                    species = ?
                    where id = ?;
                    """,
                name, updatePet.getAge(), updatePet.getSpecies(), updatePet.getId()
            ));
        var pet = getPet(name);
        log.info("Updated pet as {}", pet);
        return pet;
    }

    private <T> Optional<Pet> getPetByField(
        final String name,
        final T value
    ) {
        var sql = "SELECT * FROM pet WHERE {field} = :{field} "
            .replaceAll("\\{field\\}", name);
        var pet = jdbi.withHandle(handle ->
            handle.createQuery(sql)
                .bind(name, value)
                .mapToBean(Pet.class)
                .stream()
                .findFirst()
        );
        log.info("Pet {} retrieved", pet);

        return pet;
    }
}
