package io.baris.petclinic.dropwizard.system;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Utilities for Pet Clinic application
 */
public class PetClinicUtils {

    public static String readFileToString(final String path) {
        return escapeException(() ->
            isClasspathResource(path) ?
                readClasspathFileToString(extractPath(path)) :
                Files.readString(Paths.get(path))
        );
    }

    public static String resourceFilePath(final String path) {
        return escapeException(() -> {
            if (isClasspathResource(path)) {
                return new File(
                    getContextClassLoader().getResource(extractPath(path)).toURI()
                ).getAbsolutePath();
            } else {
                return new File(path).getAbsolutePath();
            }
        });
    }

    private static String extractPath(String path) {
        return path.substring(10);
    }

    private static boolean isClasspathResource(String path) {
        return path.startsWith("classpath:");
    }

    public static String readClasspathFileToString(String path) {
        return escapeException(() -> {
            try (var inputStream = getContextClassLoader().getResourceAsStream(path)) {
                return new String(inputStream.readAllBytes(), UTF_8);
            }
        });
    }

    private static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
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
}
