package br.com.alura.forum.config.swagger;


import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfigurations {

//    @Bean
//    public GroupedOpenApi forumApi() {
//        return GroupedOpenApi.builder()
//                .group("forumApi")
//                .pathsToMatch("/**")
//                .build();
//    }
//
//    @Bean
//    public OpenAPI forumAluraOpenAPI() {
//        return new OpenAPI()
//                .info(new Info().title("Forum API")
//                .description("Projeto de Documentacao de API da Alura")
//                .version("v0.0.1")
//                .license(new License().name("Apache 2.0").url("http://springdoc.org")))
//                .components(new Components().addSecuritySchemes("bearer-key", new SecurityScheme().type(SecurityScheme.Type.HTTP)
//                    .scheme("bearer")
//                    .bearerFormat("JWT")))
//                .externalDocs(new ExternalDocumentation()
//                    .description("SpringShop Wiki Documentation")
//                    .url("https://springshop.wiki.github.org/docs"))
//                .components(new Components()
//                        .addSecuritySchemes("bearer-key", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").in(SecurityScheme.In.HEADER).bearerFormat("JWT")));
//    }

    @Value("${info.app.version}")
    private String version;

    @Value("${springdoc.info.description}")
    private String description;

    @Value("${springdoc.info.title}")
    private String title;

    @Bean
    public OpenAPI infoApi() {

        Info info = new Info();
        info.setTitle(title);

        SecurityRequirement securityItem = new SecurityRequirement();
        securityItem.addList("Authorization");

        Components components = new Components();
        components.addSecuritySchemes("Authorization", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"));

        return new OpenAPI().components(components)
                .addSecurityItem(securityItem)
                .info(info.description(description).version(version));
    }

}
