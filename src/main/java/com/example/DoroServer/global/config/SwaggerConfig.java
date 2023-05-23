package com.example.DoroServer.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private static final String API_NAME = "DORO API";
    private static final String API_VERSION = "0.0.1";
    private static final String API_DESCRIPTION = "DORO API 명세서";

    @Bean
    public Docket api() {
        Server serverLocal = new Server("local", "http://localhost:8088", "for local usages", Collections.emptyList(), Collections.emptyList());
        return new Docket(DocumentationType.OAS_30)
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(List.of(apiKey()))
                .ignoredParameterTypes(AuthenticationPrincipal.class)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build().apiInfo(apiInfo());
    }



    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(API_NAME)
                .description(API_DESCRIPTION)
                .version(API_VERSION)
                .build();
    }



    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }


    private List<SecurityReference> defaultAuth() {
        // 권한 범위 설정
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = { authorizationScope };
        return List.of(new SecurityReference("Authorization", authorizationScopes));
    }

    /**
     * Swagger에서 사용할 인증 정보
     * Bearer {accessToken}
     */
    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Bearer", "header");
    }


}