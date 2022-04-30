package io.baris.petclinic.testing;

import io.baris.petclinic.pet.PetManager;
import io.baris.petclinic.pet.model.CreatePet;
import io.baris.petclinic.pet.model.Pet;
import io.baris.petclinic.pet.model.Species;
import io.baris.petclinic.vet.VetManager;
import io.baris.petclinic.vet.model.CreateVet;
import io.baris.petclinic.vet.model.Vet;
import io.baris.petclinic.visit.VisitDao;
import io.baris.petclinic.visit.model.MakeVisit;
import io.baris.petclinic.visit.model.Visit;
import lombok.Getter;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.junit.rules.ExternalResource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Junit rule to start PostgreSQL Docker container
 */
public class PostgreRule extends ExternalResource {

    @Getter
    private final PostgreSQLContainer container;

    @Getter
    private Jdbi jdbi;

    private VetManager vetManager;
    private PetManager petManager;
    private VisitDao visitDao;

    public PostgreRule(final String configPath) {
        this.container = PostgreUtils.getPostgreSQLContainer(configPath);
        this.container.start();
    }

    @Override
    protected void before() {
        this.jdbi = Jdbi.create(container.getJdbcUrl(), container.getUsername(), container.getPassword());
        this.jdbi.installPlugin(new SqlObjectPlugin());

        this.vetManager = new VetManager(jdbi);
        this.petManager = new PetManager(jdbi);
        this.visitDao = new VisitDao(jdbi);

        initDbSchema();
    }

    @Override
    protected void after() {
        container.stop();
    }

    public Optional<Vet> getVet(final String name) {
        return vetManager.getVet(name);
    }

    public void addVet(final String name) {
        vetManager.createVet(
            CreateVet.builder()
                .name(name)
                .build()
        );
    }

    public Optional<Pet> getPet(final String name) {
        return petManager.getPet(name);
    }

    public void addPet(
        final String name,
        final int age,
        final Species species
    ) {
        petManager.createPet(
            CreatePet.builder()
                .name(name)
                .age(age)
                .species(species)
                .build()
        );
    }

    public List<Visit> getPetVisits(final int petId) {
        return visitDao.getPetVisits(petId);
    }

    public void addPetVisit(
        final Pet pet,
        final Vet vet,
        final Instant date,
        final String treatment
    ) {
        visitDao.makeVisit(
            MakeVisit.builder()
                .petId(pet.getId())
                .vetId(vet.getId())
                .date(date)
                .treatment(treatment)
                .build()
        );
    }

    private void initDbSchema() {
        PostgreUtils.applyDbFile(jdbi, "database/tables.sql");
    }
}
