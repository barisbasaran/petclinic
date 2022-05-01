package io.baris.petclinic.testing;

import org.yaml.snakeyaml.Yaml;

import java.util.Map;

import static io.baris.petclinic.system.PetClinicUtils.readFileToString;

public class TestUtils {

    public static final String TEST_CONFIG = "test-config.yml";
    public static final int UNPROCESSIBLE_ENTITY = 422;

    public static Map<String, Object> loadConfig(final String path) {
        return new Yaml().load(readFileToString(path));
    }
}
