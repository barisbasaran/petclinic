package io.baris.petclinic.vet;

import io.baris.petclinic.vet.model.Vet;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

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
}
