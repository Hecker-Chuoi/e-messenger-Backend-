package com.e_messenger.code.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenAPIConfig(
            @Value("${api-config.title}") String title,
            @Value("${api-config.description}") String description,
            @Value("${api-config.version}") String version
    ){
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .description(description)
                        .version(version))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "user token",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }

    @Bean
    public GroupedOpenApi all() {
        return GroupedOpenApi.builder()
                .group("All")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public GroupedOpenApi auth() {
        return GroupedOpenApi.builder()
                .group("Authentication")
                .pathsToMatch("/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi user() {
        return GroupedOpenApi.builder()
                .group("User")
                .pathsToMatch("/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi chat() {
        return GroupedOpenApi.builder()
                .group("Chatting")
                .pathsToMatch("/chat/**")
                .build();
    }

    @Bean
    public GroupedOpenApi conversationQuery() {
        return GroupedOpenApi.builder()
                .group("Conversation query")
                .pathsToMatch("/conversation/**")
                .build();
    }

    @Bean
    public GroupedOpenApi directChat() {
        return GroupedOpenApi.builder()
                .group("Direct chat")
                .pathsToMatch("/direct/**")
                .build();
    }

    @Bean
    public GroupedOpenApi groupChat() {
        return GroupedOpenApi.builder()
                .group("Group chat")
                .pathsToMatch("/group/**")
                .build();
    }
}
