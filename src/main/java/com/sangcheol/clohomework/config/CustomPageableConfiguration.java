package com.sangcheol.clohomework.config;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class CustomPageableConfiguration extends SpringDataWebAutoConfiguration {

    // pageable 객체의 파라메터명을 과제 내용에 맞게 변경하기 위한 설정
    public CustomPageableConfiguration(SpringDataWebProperties properties) {
        super(properties);
        properties.getPageable().setSizeParameter("pageSize");
        properties.getPageable().setPageParameter("page");
    }
}
