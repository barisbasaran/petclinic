package io.baris.petclinic.system;

import org.jdbi.v3.core.Jdbi;

import java.util.Arrays;

import static io.baris.petclinic.system.PetClinicUtils.readFileToString;

/**
 * Utilities for Postgre database
 */
public class PostgreUtils {

    public static void applySqlScript(final Jdbi jdbi, final String path) {
        String tables = readFileToString(path);
        jdbi.withHandle(handle -> {
            Arrays.stream(tables.split(";")).forEach(handle::execute);
            return 1;
        });
    }
}
