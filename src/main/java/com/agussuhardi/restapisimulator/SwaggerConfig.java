package com.agussuhardi.restapisimulator;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    final String securitySchemeName = "Authentication";
    return new OpenAPI()
        .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
        .components(
            new Components()
                .addSecuritySchemes(
                    securitySchemeName,
                    new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
        .info(new Info().title("Rest Simulator").version("0.1"));
  }

//  @Bean
//  public GroupedOpenApi rest() {
//    String[] paths = {"/api/**"};
//    return GroupedOpenApi.builder().group("Rest").pathsToMatch(paths).build();
//  }
//
//  @Bean
//  public GroupedOpenApi web() {
//    String[] paths = {"/web/**"};
//    return GroupedOpenApi.builder().group("Web").pathsToMatch(paths).build();
//  }
}
