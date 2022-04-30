package io.baris.petclinic.visit;

import io.baris.petclinic.visit.model.MakeVisit;
import io.baris.petclinic.visit.model.Visit;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Optional;

/**
 * Manages the visits
 */
@RequiredArgsConstructor
public class VisitManager {

    private final Jdbi jdbi;

    public Optional<Visit> makeVisit(final MakeVisit makeVisit) {
        var visitId = jdbi.withExtension(VisitDao.class, dao -> dao.createVisit(
            makeVisit.getPetId(),
            makeVisit.getVetId(),
            makeVisit.getDate(),
            makeVisit.getTreatment())
        );
        var visit = jdbi.withExtension(VisitDao.class, dao -> dao.getVisit(visitId));
        return Optional.of(visit);
    }

    public List<Visit> getPetVisits(final int petId) {
        return jdbi.withExtension(VisitDao.class, dao -> dao.getPetVisits(petId));
    }
}
