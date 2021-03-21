package br.com.brainweb.interview.core.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private static final String SWAGGER_API_VERSION = "1.0";
    private static final String LICENSE_TEXT = "License";
    private static final String title = "Brainweb REST API";
    private static final String description = "Brainweb RESTful API for heroes";

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .contact(
                        new Contact("Rafael Lima",
                                "https://www.linkedin.com/in/rafaeldblima/",
                                "rafaeldblima@gmail.com"))
                .description(description)
                .license(LICENSE_TEXT)
                .version(SWAGGER_API_VERSION)
                .build();

    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.brainweb.interview.core.features.hero"))
                .paths(PathSelectors.regex("/api.*"))
                .build();
    }
}
