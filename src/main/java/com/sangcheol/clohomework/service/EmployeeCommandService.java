package com.sangcheol.clohomework.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.sangcheol.clohomework.entity.Employee;
import com.sangcheol.clohomework.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeCommandService {

    private final String DELIMITER = ",";
    private final JsonMapper mapper;
    private final EmployeeRepository employeeRepository;

    // 직원정보 생성
    public void createEmployee(MultipartFile file, String text) {
        List<Employee> employeeList = new ArrayList<>();

        if (file != null && file.getContentType().toLowerCase().contains("csv")) {  // csv 처리
            employeeList.addAll(parseEmployeeCsv(file));
        } else if (file != null && file.getContentType().toLowerCase().contains("json")) {  // json 처리
            employeeList.addAll(parseEmployeeJson(file));
        } else if (file == null && Strings.isNotBlank(text)) {  // text 처리
            employeeList.addAll(parseEmployeeText(text));
        }

        // 직원 중복 등 각종 제약에 의한 에러는 정해지는 정책에 따라 처리 또는 검증을 해야 함.
        employeeRepository.saveAll(employeeList);
    }

    private List<Employee> parseEmployeeText(String text) {
        List<Employee> empList = new ArrayList<>();
        if (text.startsWith("[")) { // json 케이스
            try {
                empList = mapper.readValue(text, new TypeReference<List<Employee>>() {});
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            BufferedReader br;
            String line;
            try {
                InputStream is = new ByteArrayInputStream(text.getBytes());
                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    String[] splitted = line.split(DELIMITER);
                    Employee emp = Employee.builder()
                            .name(splitted[0].strip())
                            .email(splitted[1].strip())
                            .tel(splitted[2].strip())
                            .joined(LocalDate.parse(splitted[3].strip(), DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                            .build();
                    empList.add(emp);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return empList;
    }

    private List<Employee> parseEmployeeJson(MultipartFile file) {
        List<Employee> empList = new ArrayList<>();
        try {
            empList = mapper.readValue(new InputStreamReader(file.getInputStream()), new TypeReference<List<Employee>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return empList;
    }

    private List<Employee> parseEmployeeCsv(MultipartFile file) {
        BufferedReader br;
        List<Employee> employeeList = new ArrayList<>();
        try {
            String line;
            InputStream is = file.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                String[] splitted = line.split(DELIMITER);
                Employee emp = Employee.builder()
                        .name(splitted[0].strip())
                        .email(splitted[1].strip())
                        .tel(splitted[2].strip())
                        .joined(LocalDate.parse(splitted[3].strip(), DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                        .build();
                employeeList.add(emp);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return employeeList;
    }

}
