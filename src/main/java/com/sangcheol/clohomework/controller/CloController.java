package com.sangcheol.clohomework.controller;

import com.sangcheol.clohomework.dto.response.CloCommonResponse;
import com.sangcheol.clohomework.dto.response.EmployeeResponseDto;
import com.sangcheol.clohomework.service.EmployeeCommandService;
import com.sangcheol.clohomework.service.EmployeeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CloController {

    private final EmployeeCommandService employeeCommandService;
    private final EmployeeQueryService employeeQueryService;

    // 직원들 연락정보 조회
    @GetMapping("/employee")
    public CloCommonResponse<Page<EmployeeResponseDto>> getEmployeeList(Pageable pageable) {
        System.out.println(pageable.getPageNumber() + pageable.getPageSize());
        return CloCommonResponse.ok(employeeQueryService.getEmployeeList(pageable));
    }

    // 직원 연락정보 조회
    @GetMapping("/employee/{name}")
    public CloCommonResponse getEmployee(@PathVariable(name = "name") String name) {
        return CloCommonResponse.ok();
    }

    // 직원 기본 연락정보 추가
    @PostMapping("/employee")
    public CloCommonResponse createEmployee() {
        return CloCommonResponse.ok();
    }

}
