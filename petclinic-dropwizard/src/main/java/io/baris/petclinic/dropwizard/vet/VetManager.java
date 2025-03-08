package io.baris.petclinic.dropwizard.vet;

import io.baris.petclinic.dropwizard.vet.model.UpdateVet;
import io.baris.petclinic.dropwizard.vet.model.Vet;
import io.baris.petclinic.dropwizard.vet.model.CreateVet;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Optional;

/**
 * Manages the vet
 */
@RequiredArgsConstructor
public class VetManager {

    private final Jdbi jdbi;

    public Optional<Vet> getVet(final int vetId) {
        return jdbi.withExtension(VetDao.class, dao -> dao.getVet(vetId));
    }

    public Optional<Vet> getVet(final String name) {
        return jdbi.withExtension(VetDao.class, dao -> dao.getVet(name));
    }

    public List<Vet> getAllVets() {
        return jdbi.withExtension(VetDao.class, VetDao::getAllVets);
    }

    public Optional<Vet> createVet(final CreateVet createVet) {
        return jdbi.withExtension(VetDao.class, dao -> dao.createVet(createVet));
    }

    public Optional<Vet> updateVet(final UpdateVet updateVet) {
        return jdbi.withExtension(VetDao.class, dao -> dao.updateVet(updateVet));
    }
}
