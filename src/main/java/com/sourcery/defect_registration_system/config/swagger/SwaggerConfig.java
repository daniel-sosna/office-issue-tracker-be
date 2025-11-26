package com.sourcery.defect_registration_system.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!prod")
public class SwaggerConfig {

    @Bean
    public OpenAPI defectRegistrationOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Defect Registration System API")
                        .description("API documentation for the defect registration system")
                        .version("0.0.1"));
    }
}
 