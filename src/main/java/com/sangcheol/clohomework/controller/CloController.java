package com.sangcheol.clohomework.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CloController {

    // 직원들 연락정보 조회
    @GetMapping("/employee")
    public void getEmployeeList() {

    }

    // 직원 연락정보 조회
    @GetMapping("/employee/{name}")
    public void getEmployee(@PathVariable(name = "name") String name) {

    }

    // 직원 기본 연락정보 추가
    @PostMapping("/employee")
    public void createEmployee() {

    }

}
