package io.baris.petclinic.visit;

import io.baris.petclinic.visit.model.Visit;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.Instant;
import java.util.List;

/**
 * Manages visit in the database
 */
public interface VisitDao {

    @SqlQuery("SELECT * FROM visit WHERE id = ?")
    @RegisterBeanMapper(Visit.class)
    Visit getVisit(int id);

    @SqlQuery("SELECT * FROM visit WHERE pet_id = ?")
    @RegisterBeanMapper(Visit.class)
    List<Visit> getPetVisits(int petId);

    @SqlUpdate("INSERT INTO visit (pet_id, vet_id, date, treatment) VALUES (?, ?, ?, ?) returning *")
    @GetGeneratedKeys
    int createVisit(int petId, int vetId, Instant date, String treatment);
}
