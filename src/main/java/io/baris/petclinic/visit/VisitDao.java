package io.baris.petclinic.visit;

import io.baris.petclinic.visit.model.MakeVisit;
import io.baris.petclinic.visit.model.Visit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

/**
 * Manages visit in the database
 */
@Slf4j
@RequiredArgsConstructor
public class VisitDao {

    private final Jdbi jdbi;

    public void makeVisit(
        final MakeVisit makeVisit
    ) {
        jdbi.withHandle(handle ->
            handle.execute("""
                     insert into visit
                     (pet_id, vet_id, date, treatment)
                     values (?, ?, ?, ?);
                    """,
                makeVisit.getPetId(),
                makeVisit.getVetId(),
                makeVisit.getDate(),
                makeVisit.getTreatment()
            ));
    }

    public List<Visit> getPetVisits(final int petId) {
        var visits = jdbi.withHandle(handle ->
            handle.createQuery("""
                    SELECT * FROM visit WHERE 
                    pet_id = :pet_id
                    ORDER BY date ASC
                    """)
                .bind("pet_id", petId)
                .mapToBean(Visit.class)
                .list()
        );
        log.debug("Retrieved visits for pet {} as {}", petId, visits);
        return visits;
    }
}
