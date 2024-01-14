package com.sangcheol.clohomework.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.sangcheol.clohomework.entity.Employee;
import com.sangcheol.clohomework.repository.EmployeeRepository;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("local")
class EmployeeCommandServiceTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JsonMapper mapper;
    private final String DELIMITER = ",";
    private final String CSV_PATH = "src/employee.csv";
    private final String JSON_PATH = "src/employee.json";

    @Autowired
    private EmployeeRepository employeeRepository;

    @AfterEach
    void cleanDb() {
        employeeRepository.deleteAll();
    }

    private void addOneData() {
        Employee basicEmp = Employee.builder()
                .email("basic@test.com")
                .name("김기본")
                .tel("010-1234-1234")
                .joined(LocalDate.now())
                .build();
        employeeRepository.saveAndFlush(basicEmp);
    }

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
        String csvText = "김철수, charles2@clovf.com, 01075312468, 2018.03.07\n" +
                "박영희, matilda2@clovf.com, 01087654321, 2021.04.28\n" +
                "홍길동, kildong.hong2@clovf.com, 01012345678, 2015.08.15";
        String jsonText = "[{\"name\":\"이무기\",\"email\":\"weapon2@clovf.com\",\"tel\":\"010-1111-2424\",\"joined\":\"2020-01-05\"},{\"name\":\"판브이\",\"email\":\"panv2@clovf.com\",\"tel\":\"010-3535-7979\",\"joined\":\"2013-07-01\"},{\"name\":\"차호빵\",\"email\":\"hobread2@clovf.com\",\"tel\":\"010-8531-7942\",\"joined\":\"2019-12-05\"}]";
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
    void text를_통한_사용자_파싱_실패케이스(){
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

    @Test
    void 사용자_목록_조회_테스트() throws Exception {
        addOneData();
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/employee")
                .param("page", "0")
                .param("size", "20"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").isNotEmpty());

    }

    @Test
    void 사용자_생성_테스트() throws Exception {
        String formText = "[{\"name\":\"이무기\",\"email\":\"weapon3@clovf.com\",\"tel\":\"010-1111-2424\",\"joined\":\"2020-01-05\"},{\"name\":\"판브이\",\"email\":\"panv3@clovf.com\",\"tel\":\"010-3535-7979\",\"joined\":\"2013-07-01\"},{\"name\":\"차호빵\",\"email\":\"hobread3@clovf.com\",\"tel\":\"010-8531-7942\",\"joined\":\"2019-12-05\"}]";
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/employee")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("text", formText)
        )
                .andExpect(MockMvcResultMatchers.status().isCreated());

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/employee")
                        .param("page", "0")
                        .param("size", "20")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").isNotEmpty());

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