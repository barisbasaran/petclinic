package io.baris.petclinic.testing;

import org.jdbi.v3.core.Jdbi;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Arrays;
import java.util.Map;

public class PostgreUtils {

    public static void applyDbFile(final Jdbi jdbi, final String path) {
        String tables = TestUtils.readFileToString(path);
        jdbi.withHandle(handle -> {
            Arrays.stream(tables.split(";")).forEach(handle::execute);
            return 1;
        });
    }

    public static PostgreSQLContainer getPostgreSQLContainer(final String configPath) {
        var config = TestUtils.loadConfig(configPath);
        var databaseConfig = (Map<String, String>) config.get("database");
        return new PostgreSQLContainer("postgres")
            .withUsername(databaseConfig.get("user"))
            .withPassword(databaseConfig.get("password"))
            .withDatabaseName((String) config.get("databaseName"));
    }
}
