package io.baris.petclinic.vet;

import io.baris.petclinic.vet.model.CreateVet;
import io.baris.petclinic.vet.model.UpdateVet;
import io.baris.petclinic.vet.model.Vet;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.util.List;
import java.util.Set;

/**
 * Manages vet in the database
 */
public interface VetDao {

    @SqlQuery("SELECT * FROM vet WHERE id = ?")
    @RegisterBeanMapper(Vet.class)
    Vet getVet(int id);

    @SqlQuery("SELECT * FROM vet WHERE name = ?")
    @RegisterBeanMapper(Vet.class)
    Vet getVet(String name);

    @SqlQuery("SELECT * FROM vet ORDER BY name")
    @RegisterBeanMapper(Vet.class)
    List<Vet> getAllVets();

    @SqlUpdate("INSERT INTO vet (name) VALUES (?) returning *")
    @GetGeneratedKeys
    int createVet(String name);

    @SqlUpdate("UPDATE vet SET name = ? WHERE id = ?")
    int updateVet(String name, int id);

    @SqlUpdate("INSERT INTO vet_specialty (vet_id, specialty) VALUES (?, ?)")
    int createVetSpecialty(int vetId, String specialty);

    @SqlUpdate("DELETE FROM vet_specialty WHERE vet_id = ?")
    int deleteVetSpecialties(int vetId);

    @SqlQuery("SELECT specialty FROM vet_specialty WHERE vet_id = ?")
    Set<String> getVetSpecialties(int vetId);

    @Transaction
    default int createVet(final CreateVet createVet) {
        var vetId = createVet(createVet.getName());
        var specialties = createVet.getSpecialties();
        createVetSpecialties(vetId, specialties);
        return vetId;
    }

    @Transaction
    default void updateVet(final UpdateVet updateVet) {
        var vetId = updateVet.getId();
        updateVet(updateVet.getName(), vetId);

        deleteVetSpecialties(vetId);
        createVetSpecialties(vetId, updateVet.getSpecialties());
    }

    default void createVetSpecialties(int vetId, Set<String> specialties) {
        if (specialties != null) {
            specialties.forEach(specialty -> createVetSpecialty(vetId, specialty));
        }
    }
}
