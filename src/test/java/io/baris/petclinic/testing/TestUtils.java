package io.baris.petclinic.testing;

import io.dropwizard.testing.ResourceHelpers;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class TestUtils {

    public static String readFileToString(final String path) {
        try {
            var tablesPath = ResourceHelpers.resourceFilePath(path);
            return Files.readString(Paths.get(tablesPath));
        } catch (IOException ex) {
            throw new RuntimeException("Error reading file %s".formatted(path), ex);
        }
    }

    public static Map<String, Object> loadConfig(final String path) {
        return new Yaml().load(readFileToString(path));
    }
}
