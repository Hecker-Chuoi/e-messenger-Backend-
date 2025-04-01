package com.e_messenger.code;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CodeApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(CodeApplication.class, args);
	}

	@Bean
	public OpenAPI openAPIConfig(){
		return new OpenAPI().info(
				new Info()
						.title("E-Messenger")
						.description("E-Messenger API")
						.version("1.0")
				)
				.components(
						new Components()
								.addSecuritySchemes(
										"admin-token",
										new SecurityScheme()
												.type(SecurityScheme.Type.HTTP)
												.scheme("bearer")
												.bearerFormat("JWT")
								)
								.addSecuritySchemes(
										"user-token",
										new SecurityScheme()
												.type(SecurityScheme.Type.HTTP)
												.scheme("bearer")
												.bearerFormat("JWT")
								)
				);
	}
}