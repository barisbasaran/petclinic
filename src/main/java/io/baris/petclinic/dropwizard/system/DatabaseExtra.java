package io.baris.petclinic.dropwizard.system;

import lombok.Data;

@Data
public class DatabaseExtra {

    String name;
    String dockerImage;
    String initScript;
}
