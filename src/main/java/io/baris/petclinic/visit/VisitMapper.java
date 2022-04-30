package io.baris.petclinic.visit;

import io.baris.petclinic.pet.PetManager;
import io.baris.petclinic.vet.VetManager;
import io.baris.petclinic.visit.model.MakeVisit;
import io.baris.petclinic.visit.model.MakeVisitRequest;
import io.baris.petclinic.visit.model.Visit;
import lombok.RequiredArgsConstructor;

/**
 * Maps visit api classes
 */
@RequiredArgsConstructor
public class VisitMapper {

    private final PetManager petManager;
    private final VetManager vetManager;

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

    public Visit mapToVisit(
        final MakeVisit makeVisit
    ) {
        var pet = petManager.getPet(makeVisit.getPetId());
        var vet = vetManager.getVet(makeVisit.getVetId());
        return Visit.builder()
            .pet(pet.get())
            .vet(vet.get())
            .date(makeVisit.getDate())
            .treatment(makeVisit.getTreatment())
            .build();
    }
}
