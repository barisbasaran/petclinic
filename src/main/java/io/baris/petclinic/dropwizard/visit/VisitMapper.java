package io.baris.petclinic.dropwizard.visit;

import io.baris.petclinic.dropwizard.visit.model.MakeVisit;
import io.baris.petclinic.dropwizard.visit.model.MakeVisitRequest;
import lombok.RequiredArgsConstructor;

/**
 * Maps visit api classes
 */
@RequiredArgsConstructor
public class VisitMapper {

    public static MakeVisit mapToMakeVisit(
        final int petId,
        final int vetId,
        final MakeVisitRequest makeVisitRequest
    ) {
        return MakeVisit.builder()
            .petId(petId)
            .vetId(vetId)
            .date(makeVisitRequest.getDate())
            .treatment(makeVisitRequest.getTreatment())
            .build();
    }
}
