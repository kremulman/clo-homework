package com.sangcheol.clohomework.controller;

import com.sangcheol.clohomework.common.CommonResponse;
import com.sangcheol.clohomework.dto.response.EmployeeResponseDto;
import com.sangcheol.clohomework.service.EmployeeCommandService;
import com.sangcheol.clohomework.service.EmployeeQueryService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CloController {

    private final EmployeeCommandService employeeCommandService;
    private final EmployeeQueryService employeeQueryService;

    // 직원들 연락정보 조회
    @GetMapping("/employee")
    @ResponseStatus(value = HttpStatus.OK)
    public CommonResponse<Page<EmployeeResponseDto>> getEmployeeList(Pageable pageable) {
        return CommonResponse.ok(employeeQueryService.getEmployeeList(pageable));
    }

    // 직원 연락정보 조회
    @GetMapping("/employee/{name}")
    @ResponseStatus(value = HttpStatus.OK)
    public CommonResponse getEmployee(@PathVariable(name = "name") String name) {
        return CommonResponse.ok(employeeQueryService.getEmployee(name));
    }

    // 직원 기본 연락정보 추가
    @PostMapping("/employee")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponse createEmployee(MultipartFile file, String text) {
        // 프론트에서 file or text 처리를 해주지 않으면 어떤 값이 옳은지 알 수 없기 때문에 파일과 text 파라메터가 다 있으면 에러처리
        if (file != null && Strings.isNotBlank(text)) {
            throw new RuntimeException("파일과 text 한 가지 방식만 등록 가능합니다.");
        }
        employeeCommandService.createEmployee(file, text);
        return CommonResponse.ok();
    }

}
