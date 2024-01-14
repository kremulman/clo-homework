package com.sangcheol.clohomework.common.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class CommonLogging {

    private final ObjectMapper mapper;

    @Pointcut("within(com.sangcheol.clohomework.controller..*)")
    private void cut(){

    }

    //logging - console
    @Around("cut()")
    public Object logging(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        log.info("-----------> REQUEST : {}({}), param : {}", req.getMethod(), req.getRequestURI(), parseParam(req));
        Object result = pjp.proceed();
        log.info("-----------> RESPONSE : {}({}), body : {}", req.getMethod(), req.getRequestURI(), parseResult(result));
        return result;
    }

    private String parseResult(Object result) {
        try {
            return mapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            return "failed to parse response";
        }
    }

    private Object parseParam(HttpServletRequest req) {
        return req.getParameterMap()
                .entrySet()
                .stream()
                .map(e -> e.getKey() + " : " + Arrays.stream(e.getValue()).collect(Collectors.joining(",")))
                .collect(Collectors.joining(","));
    }

}
