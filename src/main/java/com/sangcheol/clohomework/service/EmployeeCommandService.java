package com.sangcheol.clohomework.service;

import com.sangcheol.clohomework.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeCommandService {

    private final EmployeeRepository employeeRepository;

    // 직원정보 생성
    public void createEmployee() {

    }

}
