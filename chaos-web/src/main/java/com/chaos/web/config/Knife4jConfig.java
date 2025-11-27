package com.chaos.web.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j API文档配置
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SpringBoot-Chaos API")
                        .description("SpringBoot脚手架项目 - 基于Sa-Token的轻量级认证授权框架")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Chaos")
                                .email("admin@chaos.com"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .schemaRequirement("Authorization", new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .name("Authorization")
                        .in(SecurityScheme.In.HEADER)
                        .description("Token认证"))
                .addSecurityItem(new SecurityRequirement().addList("Authorization"));
    }
}
