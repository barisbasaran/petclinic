package io.baris.petclinic.system;

import io.dropwizard.util.Resources;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PetClinicUtils {

    public static String readFileToString(final String path) {
        try {
            var absolutePath = resourceFilePath(path);
            return Files.readString(Paths.get(absolutePath));
        } catch (IOException ex) {
            throw new RuntimeException("Error reading file %s".formatted(path), ex);
        }
    }

    private static String resourceFilePath(final String resourceClassPathLocation) {
        try {
            return new File(Resources.getResource(resourceClassPathLocation).toURI())
                .getAbsolutePath();
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException(e);
        }
    }
}
