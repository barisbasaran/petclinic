package io.baris.petclinic.vet;

import io.baris.petclinic.vet.model.CreateVet;
import io.baris.petclinic.vet.model.UpdateVet;
import io.baris.petclinic.vet.model.Vet;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Manages the vet
 */
@RequiredArgsConstructor
public class VetManager {

    private final Jdbi jdbi;

    public Optional<Vet> getVet(final int vetId) {
        var vet = jdbi.withExtension(VetDao.class, dao -> dao.getVet(vetId));
        if (vet != null) {
            vet.setSpecialties(getVetSpecialities(vetId));
        }
        return Optional.ofNullable(vet);
    }

    public Optional<Vet> getVet(final String name) {
        var vet = jdbi.withExtension(VetDao.class, dao -> dao.getVet(name));
        if (vet != null) {
            vet.setSpecialties(getVetSpecialities(vet.getId()));
        }
        return Optional.ofNullable(vet);
    }

    public List<Vet> getAllVets() {
        return jdbi.withExtension(VetDao.class, VetDao::getAllVets)
            .stream()
            .peek(vet -> vet.setSpecialties(getVetSpecialities(vet.getId())))
            .toList();
    }

    @Transaction
    public Optional<Vet> createVet(
        final CreateVet createVet
    ) {
        try (var handle = jdbi.open()) {
            var vetDao = handle.attach(VetDao.class);
            var vetId = vetDao.createVet(createVet);
            return getVet(vetId);
        }
    }

    public Optional<Vet> updateVet(
        final UpdateVet updateVet
    ) {
        try (var handle = jdbi.open()) {
            var vetDao = handle.attach(VetDao.class);
            vetDao.updateVet(updateVet);
            return getVet(updateVet.getId());
        }
    }

    private Set<String> getVetSpecialities(int vetId) {
        return jdbi.withExtension(VetDao.class,
            dao -> dao.getVetSpecialties(vetId));
    }
}
