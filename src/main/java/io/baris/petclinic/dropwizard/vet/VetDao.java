package io.baris.petclinic.dropwizard.vet;

import io.baris.petclinic.dropwizard.vet.model.Vet;
import io.baris.petclinic.dropwizard.vet.model.CreateVet;
import io.baris.petclinic.dropwizard.vet.model.UpdateVet;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Manages vet in the database
 */
public interface VetDao {

    @SqlQuery("SELECT * FROM vet WHERE id = ?")
    @RegisterBeanMapper(Vet.class)
    Vet getVetBasic(int vetId);

    @SqlQuery("SELECT * FROM vet WHERE name = ?")
    @RegisterBeanMapper(Vet.class)
    Vet getVetBasic(String name);

    @SqlQuery("SELECT * FROM vet ORDER BY name")
    @RegisterBeanMapper(Vet.class)
    List<Vet> getAllVetsBasic();

    @SqlUpdate("INSERT INTO vet (name) VALUES (?) returning *")
    @GetGeneratedKeys
    int createVetBasic(String name);

    @SqlUpdate("UPDATE vet SET name = ? WHERE id = ?")
    void updateVetBasic(String name, int id);

    @SqlUpdate("INSERT INTO vet_specialty (vet_id, specialty) VALUES (?, ?)")
    void createVetSpecialty(int vetId, String specialty);

    @SqlUpdate("DELETE FROM vet_specialty WHERE vet_id = ?")
    void deleteVetSpecialties(int vetId);

    @SqlQuery("SELECT specialty FROM vet_specialty WHERE vet_id = ?")
    Set<String> getVetSpecialties(int vetId);

    @Transaction
    default Optional<Vet> getVet(final int vetId) {
        var vet = getVetBasic(vetId);
        if (vet != null) {
            vet.setSpecialties(getVetSpecialties(vetId));
        }
        return Optional.ofNullable(vet);
    }

    @Transaction
    default Optional<Vet> getVet(final String name) {
        var vet = getVetBasic(name);
        return vet != null ? getVet(vet.getId()) : Optional.empty();
    }

    @Transaction
    default List<Vet> getAllVets() {
        return getAllVetsBasic()
            .stream()
            .map(vet -> getVet(vet.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
    }

    @Transaction
    default Optional<Vet> createVet(final CreateVet createVet) {
        var vetId = createVetBasic(createVet.getName());

        var specialties = createVet.getSpecialties();
        createVetSpecialties(vetId, specialties);

        return getVet(vetId);
    }

    @Transaction
    default Optional<Vet> updateVet(final UpdateVet updateVet) {
        var vetId = updateVet.getId();
        updateVetBasic(updateVet.getName(), vetId);

        deleteVetSpecialties(vetId);
        createVetSpecialties(vetId, updateVet.getSpecialties());

        return getVet(vetId);
    }

    default void createVetSpecialties(int vetId, Set<String> specialties) {
        if (specialties != null) {
            specialties.forEach(specialty ->
                createVetSpecialty(vetId, specialty));
        }
    }
}
