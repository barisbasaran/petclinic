package io.baris.petclinic.dropwizard.visit;

import io.baris.petclinic.dropwizard.visit.model.MakeVisit;
import io.baris.petclinic.dropwizard.visit.model.Visit;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Manages visits in the database
 */
public interface VisitDao {

    @SqlQuery("SELECT * FROM visits WHERE id = ?")
    @RegisterBeanMapper(Visit.class)
    Visit getVisit(int id);

    @SqlQuery("SELECT * FROM visits WHERE pet_id = ?")
    @RegisterBeanMapper(Visit.class)
    List<Visit> getPetVisits(int petId);

    @SqlUpdate("INSERT INTO visits (pet_id, vet_id, date, treatment) VALUES (?, ?, ?, ?) returning *")
    @GetGeneratedKeys
    int createVisit(int petId, int vetId, Instant date, String treatment);

    @Transaction
    default Optional<Visit> createVisit(MakeVisit makeVisit) {
        var visitId = createVisit(
            makeVisit.getPetId(),
            makeVisit.getVetId(),
            makeVisit.getDate(),
            makeVisit.getTreatment()
        );
        return Optional.of(getVisit(visitId));
    }
}
