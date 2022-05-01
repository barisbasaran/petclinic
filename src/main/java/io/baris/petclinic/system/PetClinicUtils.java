package io.baris.petclinic.system;

import io.dropwizard.util.Resources;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Utilities for Pet Clinic application
 */
public class PetClinicUtils {

    public static String readFileToString(final String path) {
        return escapeException(() ->
            Files.readString(Paths.get(resourceFilePath(path)))
        );
    }

    public interface ThrowingSupplier<T> {
        T get() throws Exception;
    }

    public static <T> T escapeException(ThrowingSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException(e);
        }
    }

    private static String resourceFilePath(final String resourceClassPathLocation) {
        return escapeException(() ->
            new File(Resources.getResource(resourceClassPathLocation).toURI()).getAbsolutePath());
    }
}
