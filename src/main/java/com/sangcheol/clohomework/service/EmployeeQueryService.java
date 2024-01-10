package com.sangcheol.clohomework.service;

import com.sangcheol.clohomework.dto.response.EmployeeResponseDto;
import com.sangcheol.clohomework.entity.Employee;
import com.sangcheol.clohomework.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeQueryService {

    private final EmployeeRepository employeeRepository;

    public Page<EmployeeResponseDto> getEmployeeList(Pageable page) {
        Page<Employee> employeeList = employeeRepository.findAll(page);
        return employeeList.map(EmployeeResponseDto::from);
    }

}
