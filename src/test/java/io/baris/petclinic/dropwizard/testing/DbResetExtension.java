package io.baris.petclinic.dropwizard.testing;

import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static io.baris.petclinic.dropwizard.system.PostgreUtils.applySqlScript;

/**
 * Junit rule to truncate DB tables to clean them for a fresh start for each test case
 */
@RequiredArgsConstructor
public class DbResetExtension implements BeforeEachCallback {

    private final Jdbi jdbi;

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        applySqlScript(jdbi, "classpath:database/db-reset.sql");
    }
}
