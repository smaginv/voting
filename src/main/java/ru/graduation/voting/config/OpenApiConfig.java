package ru.graduation.voting.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
@OpenAPIDefinition(
        info = @Info(
                title = "REST API documentation",
                version = "1.0",
                description = "Voting System"
        ),
        security = @SecurityRequirement(name = "basicAuth")
)
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi adminApi() {
        String[] excludedPaths = {"/api/profile/register", "/api/votes"};
        return GroupedOpenApi.builder()
                .group("admin")
                .pathsToMatch("/api/**")
                .pathsToExclude(excludedPaths)
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        String[] paths = {"/api/profile", "/api/votes",
                "/api/restaurants/menu-on-date", "/api/restaurants/menu-today"};
        return GroupedOpenApi.builder()
                .group("user")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi anonymousApi() {
        return GroupedOpenApi.builder()
                .group("anonymous")
                .pathsToMatch("/api/profile/register")
                .build();
    }
}
