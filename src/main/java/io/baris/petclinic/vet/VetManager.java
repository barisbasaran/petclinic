package io.baris.petclinic.vet;

import io.baris.petclinic.vet.model.CreateVet;
import io.baris.petclinic.vet.model.Vet;
import io.baris.petclinic.vet.model.UpdateVet;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * Manages the vet
 */
@RequiredArgsConstructor
public class VetManager {

    private final VetDao vetDao;

    public Optional<Vet> getVet(final int id) {
        return vetDao.getVet(id);
    }

    public List<Vet> getAllVets() {
        return vetDao.getAllVets();
    }

    public Optional<Vet> createVet(
        final CreateVet createVet
    ) {
        return vetDao.createVet(createVet);
    }

    public Optional<Vet> updateVet(
        final UpdateVet updateVet
    ) {
        return vetDao.updateVet(updateVet);
    }
}
