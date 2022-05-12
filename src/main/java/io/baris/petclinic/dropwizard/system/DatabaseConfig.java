package io.baris.petclinic.dropwizard.system;

import lombok.Data;

@Data
public class DatabaseConfig {

    String name;
    String dockerImage;
    String initScript;
}
