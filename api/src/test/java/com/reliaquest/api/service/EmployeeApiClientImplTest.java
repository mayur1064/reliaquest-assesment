package com.reliaquest.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.dto.EmployeeRequest;
import com.reliaquest.api.model.dto.ServerResponse;
import com.reliaquest.api.util.MockDataHelper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

public class EmployeeApiClientImplTest {

    private RestTemplate restTemplate;
    private IEmployeeApiClient employeeApiClient;

    private MockDataHelper mockDataHelper;

    private final String BASE_URL = "http://localhost:8112/api/v1/employee";

    @BeforeEach
    void setup() {
        restTemplate = mock(RestTemplate.class);
        employeeApiClient = new EmployeeApiClientImpl(restTemplate);
        ReflectionTestUtils.setField(employeeApiClient, "BASE_URL", "http://localhost:8112/api/v1/employee");
        mockDataHelper = new MockDataHelper();
    }

    @Test
    void testGetAllEmployees() {
        Employee emp = new Employee("12345", "Mayur", 1000, 25, "Engineer", "mayur.bhor@reliaquest.com");
        ServerResponse<List<Employee>> response = new ServerResponse<>(List.of(emp), "ok");

        when(restTemplate.exchange(eq(BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        List<Employee> employees = employeeApiClient.getAllEmployees();
        assertEquals(1, employees.size());
        assertEquals("Mayur", employees.get(0).getEmployee_name());
    }

    @Test
    void testGetEmployeeById() {
        Employee emp = new Employee("12345", "Mayur", 1000, 25, "Engineer", "mayur.bhor@reliaquest.com");
        ServerResponse<Employee> response = new ServerResponse<>(emp, "ok");

        when(restTemplate.exchange(
                        eq(BASE_URL + "/" + "12345"),
                        eq(HttpMethod.GET),
                        isNull(),
                        any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        Employee employee = employeeApiClient.getEmployeeById("12345");
        assertEquals("Mayur", employee.getEmployee_name());
    }

    @Test
    void testGetEmployeesByNameSearch() {
        Employee emp = new Employee("12345", "Mayur", 1000, 25, "Engineer", "mayur.bhor@reliaquest.com");
        ServerResponse<List<Employee>> response = new ServerResponse<>(List.of(emp), "ok");

        when(restTemplate.exchange(eq(BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        List<Employee> employees = employeeApiClient.getEmployeesByNameSearch("Ma");
        assertEquals("Mayur", employees.get(0).getEmployee_name());
    }

    @Test
    void testGetHighestSalaryOfEmployees() {
        List<Employee> employees = mockDataHelper.getEmployeeMockData();
        ServerResponse<List<Employee>> resp = new ServerResponse<>(employees, "ok");

        when(restTemplate.exchange(eq(BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(resp, HttpStatus.OK));

        Integer highest = employeeApiClient.getHighestSalaryOfEmployees();
        assertEquals(14000, highest);
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames() {
        List<Employee> employees = mockDataHelper.getEmployeeMockData();
        ServerResponse<List<Employee>> response = new ServerResponse<>(employees, "ok");

        when(restTemplate.exchange(eq(BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        List<String> topTenEarners = employeeApiClient.getTopTenHighestEarningEmployeeNames();

        assertTrue(topTenEarners.contains("Employee5"));
        assertTrue(topTenEarners.contains("Employee6"));
        assertTrue(topTenEarners.contains("Employee14"));
        assertFalse(topTenEarners.contains("Employee1"));
    }

    @Test
    void testCreateEmployee() {
        Employee employee = new Employee("12345", "Mayur", 1000, 25, "Engineer", "mayur.bhor@reliaquest.com");
        ServerResponse<Employee> response = new ServerResponse<>(employee, "created");

        when(restTemplate.exchange(
                        eq(BASE_URL),
                        eq(HttpMethod.POST),
                        any(HttpEntity.class),
                        any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.CREATED));

        EmployeeRequest employeeRequest = new EmployeeRequest("Mayur", 1000, 25, "Engineer");
        Employee result = employeeApiClient.createEmployee(employeeRequest);

        assertEquals("Mayur", result.getEmployee_name());
        assertEquals(1000, result.getEmployee_salary());
    }

    @Test
    void testDeleteEmployeeById() {
        Employee employee = new Employee("12345", "Mayur", 1000, 25, "Engineer", "mayur.bhor@reliaquest.com");
        ServerResponse<Employee> response = new ServerResponse<>(employee, "success");

        when(restTemplate.exchange(
                        eq(BASE_URL + "/" + "12345"),
                        eq(HttpMethod.GET),
                        isNull(),
                        any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        when(restTemplate.exchange(
                        eq(BASE_URL),
                        eq(HttpMethod.DELETE),
                        any(HttpEntity.class),
                        any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(new ServerResponse<Boolean>(true, "deleted"), HttpStatus.OK));

        boolean deleted = employeeApiClient.deleteEmployeeById("12345");
        assertTrue(deleted);
    }
}
