package io.baris.petclinic.dropwizard.testing;

import io.baris.petclinic.dropwizard.system.PetclinicConfiguration;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import static io.baris.petclinic.dropwizard.system.PetClinicUtils.readFileToString;

public class TestUtils {

    public static final String TEST_CONFIG = "classpath:test-config.yml";
    public static final int UNPROCESSIBLE_ENTITY = 422;

    public static PetclinicConfiguration loadConfig(final String path) {
        var representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(true);

        return new Yaml(representer)
            .loadAs(readFileToString(path), PetclinicConfiguration.class);
    }
}
