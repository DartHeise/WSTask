package com.ws.task.config;

import com.google.common.collect.Sets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Конфигурация для Swagger
 *
 * @author Maxim Seredkin
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .build()
                .produces(Sets.newHashSet(APPLICATION_JSON_VALUE))
                .consumes(Sets.newHashSet(APPLICATION_JSON_VALUE));
    }
}
