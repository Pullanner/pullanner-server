package com.pullanner.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
            .group("v1-definition")
            .pathsToMatch("/api/**")
            .build();
    }

    @Bean
    public OpenAPI pullannerOpenAPI() {
        Info info = new Info()
            .title("Pullanner Open API")
            .description("Pullanner 프로젝트 API 명세서")
            .version("v0.0.1");

        String accessTokenSchemeName = "access token";
        String refreshTokenSchemeName = "refresh token";

        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.addList(accessTokenSchemeName);
        securityRequirement.addList(refreshTokenSchemeName);

        Components components = new Components();
        components.addSecuritySchemes(accessTokenSchemeName, new SecurityScheme()
            .name(accessTokenSchemeName)
            .type(Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
        );

        components.addSecuritySchemes(refreshTokenSchemeName, new SecurityScheme()
            .name(refreshTokenSchemeName)
            .type(Type.APIKEY)
            .in(In.COOKIE)
            .name("renew")
        );

        return new OpenAPI()
            .info(info)
            .addSecurityItem(securityRequirement)
            .components(components);
    }
}
