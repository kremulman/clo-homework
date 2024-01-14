package com.sangcheol.clohomework;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@EnableAspectJAutoProxy
@ControllerAdvice(basePackages = {"com.sangcheol.clohomework.controller.*"})
public class ClohomeworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClohomeworkApplication.class, args);
    }

    // 실제 운영에서는 파싱하는 데이터의 타입이나 파서의 요구조건이 다를 수 있으나 과제 케이스에서는 모두 동일한 조건일 것이라 가정해서
    // 빈 생성을 해 놓고 사용한다.
    @Bean
    public JsonMapper parsingJsonMapper() {
        JsonMapper mapper = new JsonMapper();
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE));

        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(timeModule);

        return mapper;
    }
}
