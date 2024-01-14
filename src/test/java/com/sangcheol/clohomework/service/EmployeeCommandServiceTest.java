package com.sangcheol.clohomework.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.sangcheol.clohomework.entity.Employee;
import com.sangcheol.clohomework.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
class EmployeeCommandServiceTest {

    @Autowired
    private JsonMapper mapper;
    private final String DELIMITER = ",";
    private final String CSV_PATH = "src/employee.csv";
    private final String JSON_PATH = "src/employee.json";

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void csv_파일을_통한_사용자_생성() throws IOException {
        MockMultipartFile mockFile =
                new MockMultipartFile(
                        "employee",
                        "employee.csv",
                        MediaType.MULTIPART_FORM_DATA_VALUE,
                        new FileInputStream(CSV_PATH)
                );
        List<Employee> employeeList = parseEmployeeCsv(mockFile);
        employeeList = employeeRepository.saveAllAndFlush(employeeList);
        List<Employee> dbList = employeeRepository.findAll();

        assertFalse(employeeList.isEmpty());
        assertEquals(employeeList.size(), dbList.size());
    }

    @Test
    void json_파일을_통한_사용자_생성() throws IOException {
        MockMultipartFile mockFile =
                new MockMultipartFile(
                        "employee",
                        "employee.json",
                        MediaType.MULTIPART_FORM_DATA_VALUE,
                        new FileInputStream(JSON_PATH)
                );
        List<Employee> employeeList = parseEmployeeJson(mockFile);
        employeeRepository.saveAllAndFlush(employeeList);
        List<Employee> dbList = employeeRepository.findAll();

        assertFalse(employeeList.isEmpty());
        assertEquals(employeeList.size(), dbList.size());
    }

    @Test
    void text를_통한_사용자_파싱_및_생성(){
        String csvText = "김철수, charles@clovf.com, 01075312468, 2018.03.07\n" +
                "박영희, matilda@clovf.com, 01087654321, 2021.04.28\n" +
                "홍길동, kildong.hong@clovf.com, 01012345678, 2015.08.15";
        String jsonText = "[{\"name\":\"이무기\",\"email\":\"weapon@clovf.com\",\"tel\":\"010-1111-2424\",\"joined\":\"2020-01-05\"},{\"name\":\"판브이\",\"email\":\"panv@clovf.com\",\"tel\":\"010-3535-7979\",\"joined\":\"2013-07-01\"},{\"name\":\"차호빵\",\"email\":\"hobread@clovf.com\",\"tel\":\"010-8531-7942\",\"joined\":\"2019-12-05\"}]";
        List<Employee> csvEmployeeList = parseEmployeeText(csvText);
        List<Employee> jsonEmployeeList = parseEmployeeText(jsonText);


        employeeRepository.saveAllAndFlush(csvEmployeeList);
        employeeRepository.saveAllAndFlush(jsonEmployeeList);

        List<Employee> dbList = employeeRepository.findAll();

        assertFalse(jsonEmployeeList.isEmpty());
        assertFalse(csvEmployeeList.isEmpty());

        assertEquals(csvEmployeeList.size() + jsonEmployeeList.size(), dbList.size());
    }
    @Test
    void text를_통한_사용자_파싱_및_생성_실패케이스(){
        String csvText = "김철수, charles@clovf.com, 01075312468 2018.03.07\n" +
                "박영희, matilda@clovf.com, 01087654321, 2021.04.28\n" +
                "홍길동, kildong.hong@clovf.com, 01012345678, 2015.08.15";
        String jsonText = "[{\"name\":\"이무기\",\"email\":\"weapon@clovf.com\",\"tel\":\"010-1111-2424\",\"joined\":\"2020-01-05\"},{\"name\":\"판브이\",\"email\":\"panv@clovf.com\",\"tel\":\"010-3535-7979\",\"joined\":\"2013-07-01\"},{\"name\":\"차호빵\",\"email\":\"hobread@clovf.com\",\"tel\":\"010-8531-7942\",\"joined 2019-12-05\"}]";
        assertThrows(RuntimeException.class, () -> {
            parseEmployeeText(csvText);
        });
        assertThrows(RuntimeException.class, () -> {
            parseEmployeeText(jsonText);
        });
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