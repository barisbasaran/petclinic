package io.baris.petclinic.testing;

import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;
import org.junit.rules.ExternalResource;

import static io.baris.petclinic.system.PostgreUtils.applyDbFile;

/**
 * Junit rule to truncate DB tables to clean them for a fresh start for each test case
 */
@RequiredArgsConstructor
public class DbCleanupRule extends ExternalResource {

    private final Jdbi jdbi;

    @Override
    protected void before() {
        applyDbFile(jdbi, "database/reset-tables.sql");
    }
}
