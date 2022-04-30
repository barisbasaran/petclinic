package io.baris.petclinic.visit;

import io.baris.petclinic.visit.model.MakeVisit;
import io.baris.petclinic.visit.model.Visit;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * Manages the visits
 */
@RequiredArgsConstructor
public class VisitManager {

    private final VisitDao visitDao;
    private final VisitMapper visitMapper;

    public Optional<Visit> makeVisit(
        final MakeVisit makeVisit
    ) {
        return visitDao
            .makeVisit(makeVisit)
            .map(visitMapper::mapToVisit);
    }

    public List<Visit> getPetVisits(final int petId) {
        return visitDao
            .getPetVisits(petId)
            .stream()
            .map(visitMapper::mapToVisit)
            .toList();
    }
}
