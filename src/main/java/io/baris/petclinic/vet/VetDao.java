package io.baris.petclinic.vet;

import io.baris.petclinic.vet.model.CreateVet;
import io.baris.petclinic.vet.model.Vet;
import io.baris.petclinic.vet.model.UpdateVet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Optional;

/**
 * Manages vet in the database
 */
@Slf4j
@RequiredArgsConstructor
public class VetDao {

    private final Jdbi jdbi;

    public Optional<Vet> getVet(final int id) {
        return getVetByField("id", id);
    }

    public Optional<Vet> getVet(final String name) {
        return getVetByField("name", name);
    }

    public List<Vet> getAllVets() {
        var vets = jdbi.withHandle(handle ->
            handle.createQuery("SELECT * FROM vet ORDER BY name")
                .mapToBean(Vet.class)
                .list()
        );
        log.debug("Retrieved all vets as {}", vets);

        return vets;
    }

    public Optional<Vet> createVet(
        final CreateVet createVet
    ) {
        var name = createVet.getName();
        jdbi.withHandle(handle ->
            handle.execute("insert into vet (name) values (?);", name)
        );
        var vet = getVet(name);
        log.info("Created vet as {}", vet);
        return vet;
    }

    public Optional<Vet> updateVet(
        final UpdateVet updateVet
    ) {
        var name = updateVet.getName();
        jdbi.withHandle(handle ->
            handle.execute("update vet set name = ? where id = ?;",
                name, updateVet.getId())
        );
        var vet = getVet(name);
        log.info("Updated vet as {}", vet);
        return vet;
    }

    private <T> Optional<Vet> getVetByField(
        final String name,
        final T value
    ) {
        var sql = "SELECT * FROM vet WHERE {field} = :{field} "
            .replaceAll("\\{field\\}", name);
        var vet = jdbi.withHandle(handle ->
            handle.createQuery(sql)
                .bind(name, value)
                .mapToBean(Vet.class)
                .stream()
                .findFirst()
        );
        log.info("Vet {} retrieved", vet);

        return vet;
    }
}
