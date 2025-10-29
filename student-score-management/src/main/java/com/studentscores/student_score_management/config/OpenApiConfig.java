package com.studentscores.student_score_management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI studentScoresOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Student Scores API")
                        .description("REST API for managing student scores and generating statistical reports")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Student Scores Team")
                                .email("support@studentscores.com")));
    }
}