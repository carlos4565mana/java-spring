package com.carlos.security.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Carlos Santos",
                        email = "carloscal61@gmail.com"
                ),
                description = "OpenApi documentation for Spring Security",
                title = "OpenApi specification - Carlos",
                version = "1.0",
                license = @License(
                        name = "",
                        url = ""
                ),
                termsOfService = ""
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080"
                )

        }
)
public class OpenApiConfig {
}
