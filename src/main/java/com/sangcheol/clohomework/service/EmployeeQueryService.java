package com.sangcheol.clohomework.service;

import com.sangcheol.clohomework.common.CommonErrorCode;
import com.sangcheol.clohomework.common.exception.CommonCloException;
import com.sangcheol.clohomework.dto.response.EmployeeResponseDto;
import com.sangcheol.clohomework.entity.Employee;
import com.sangcheol.clohomework.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeQueryService {

    private final EmployeeRepository employeeRepository;

    public Page<EmployeeResponseDto> getEmployeeList(Pageable page) {
        Page<Employee> employeeList = employeeRepository.findAll(page);
        return employeeList.map(EmployeeResponseDto::from);
    }

    public EmployeeResponseDto getEmployee(String name) {
        return employeeRepository.findByName(name)
                .map(EmployeeResponseDto::from)
                .orElseThrow(
                        () -> new CommonCloException(CommonErrorCode.NOT_FOUND, CommonErrorCode.NOT_FOUND.getFormattedMessage("직원"))
                );
    }
}
