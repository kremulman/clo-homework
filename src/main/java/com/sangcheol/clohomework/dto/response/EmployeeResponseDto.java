package com.sangcheol.clohomework.dto.response;

import com.sangcheol.clohomework.entity.Employee;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class EmployeeResponseDto {

    private String name;
    private String email;
    private String tel;
    private LocalDate joined;

    public static EmployeeResponseDto from(Employee employee) {

        return EmployeeResponseDto.builder()
                .name(employee.getName())
                .email(employee.getEmail())
                .tel(employee.getTel())
                .joined(employee.getJoined())
                .build();
    }
}
