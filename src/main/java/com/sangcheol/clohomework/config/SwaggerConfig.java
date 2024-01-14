package com.sangcheol.clohomework.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("CLOvf Homework APIs")
                .description("클로버추얼패션의 사전과제 API 명세");
        return new OpenAPI().info(info);
    }

}
