package com.first.auth.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger UI: https://localhost:8443/swagger-ui.html
 * <p>
 * Use the "Authorize" button with the {@code accessToken} returned by /api/v1/auth/login.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Auth Service API",
                version = "1.0.0",
                description = "JWT authentication service: registration, login, refresh token rotation, "
                        + "password reset, email verification and user administration. Users are stored in MongoDB.",
                contact = @Contact(name = "Auth Service")),
        servers = @Server(url = "/", description = "Current host"))
@SecurityScheme(
        name = OpenApiConfig.BEARER_AUTH,
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT")
public class OpenApiConfig {

    public static final String BEARER_AUTH = "bearerAuth";
}
