package io.baris.petclinic.vet;

import io.baris.petclinic.vet.model.CreateVet;
import io.baris.petclinic.vet.model.UpdateVet;
import io.baris.petclinic.vet.model.Vet;
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

    public Optional<Vet> getVet(final int id) {
        var Vet = jdbi.withExtension(VetDao.class, dao -> dao.getVet(id));
        return Optional.ofNullable(Vet);
    }

    public Optional<Vet> getVet(final String name) {
        var Vet = jdbi.withExtension(VetDao.class, dao -> dao.getVet(name));
        return Optional.ofNullable(Vet);
    }

    public List<Vet> getAllVets() {
        return jdbi.withExtension(VetDao.class, VetDao::getAllVets);
    }

    public Optional<Vet> createVet(
        final CreateVet createVet
    ) {
        var VetId = jdbi.withExtension(
            VetDao.class,
            dao -> dao.createVet(createVet.getName())
        );
        return getVet(VetId);
    }

    public Optional<Vet> updateVet(
        final UpdateVet updateVet
    ) {
        jdbi.withExtension(VetDao.class, dao -> dao.updateVet(
            updateVet.getName(),
            updateVet.getId()
        ));
        return getVet(updateVet.getId());
    }
}
