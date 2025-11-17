package com.niwe.erp.web.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI niweErpApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Niwe ERP API Documentation")
                        .version("1.0.0")
                        .description("API documentation for Niwe ERP modules")
                        .contact(new Contact()
                                .name("Afritech Solutions")
                                .email("support@afritech.com")
                                .url("https://afritech.com")
                        )
                );
    }
}
