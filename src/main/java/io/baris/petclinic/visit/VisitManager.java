package io.baris.petclinic.visit;

import io.baris.petclinic.visit.model.MakeVisit;
import io.baris.petclinic.visit.model.Visit;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Manages the visits
 */
@RequiredArgsConstructor
public class VisitManager {

    private final VisitDao visitDao;

    public void makeVisit(final MakeVisit makeVisit) {
        visitDao.makeVisit(makeVisit);
    }

    public List<Visit> getPetVisits(final int petId) {
        return visitDao.getPetVisits(petId);
    }
}
