package com.amigoscode.demo;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public abstract class AbstractTestContainer {

    @BeforeAll
    static void beforeAll() {
        Flyway flyway = Flyway.configure().dataSource(
                postgreSqlContainer.getJdbcUrl(),
                postgreSqlContainer.getUsername(),
                postgreSqlContainer.getPassword()
        ).load();
        flyway.migrate();
    }

    @Container
    protected static final PostgreSQLContainer<?> postgreSqlContainer =
            new PostgreSQLContainer<>("postgres")
                    .withDatabaseName("amigoscode-dao-unit-test")
                    .withUsername("amigoscode")
                    .withPassword("password");

    @DynamicPropertySource
    private static void registerDataSourceProperties(DynamicPropertyRegistry registry){
        registry.add(
                "spring.datasource.url",
                () -> postgreSqlContainer.getJdbcUrl()
        );

        registry.add(
                "spring.datasource.username",
                () -> postgreSqlContainer.getUsername()
        );

        registry.add(
                "spring.datasource.password",
                () -> postgreSqlContainer.getPassword()
        );
    }
}
