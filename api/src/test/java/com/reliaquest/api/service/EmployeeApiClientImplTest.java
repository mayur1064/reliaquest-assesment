package com.reliaquest.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.dto.EmployeeRequest;
import com.reliaquest.api.model.dto.ServerResponse;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class EmployeeApiClientImplTest {

    private RestTemplate restTemplate;
    private IEmployeeApiClient employeeApiClient;

    private final String BASE_URL = "http://localhost:8112/api/v1/employee";

    @BeforeEach
    void setup() {
        restTemplate = mock(RestTemplate.class);
        employeeApiClient = new EmployeeApiClientImpl(restTemplate);
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
        List<Employee> employees = getEmployeeMockData();
        ServerResponse<List<Employee>> resp = new ServerResponse<>(employees, "ok");

        when(restTemplate.exchange(eq(BASE_URL), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(resp, HttpStatus.OK));

        Integer highest = employeeApiClient.getHighestSalaryOfEmployees();
        assertEquals(14000, highest);
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames() {
        List<Employee> employees = getEmployeeMockData();
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

    private List<Employee> getEmployeeMockData() {
        List<Employee> employees = Arrays.asList(
                new Employee("1", "Employee1", 1000, 25, "Engineer", "employee1@reliaquest.com"),
                new Employee("2", "Employee2", 2000, 25, "Engineer", "employee2@reliaquest.com"),
                new Employee("3", "Employee3", 3000, 25, "Engineer", "employee3@reliaquest.com"),
                new Employee("4", "Employee4", 4000, 25, "Engineer", "employee4@reliaquest.com"),
                new Employee("5", "Employee5", 5000, 25, "Engineer", "employee5@reliaquest.com"),
                new Employee("6", "Employee6", 6000, 25, "Engineer", "employee6@reliaquest.com"),
                new Employee("7", "Employee7", 7000, 25, "Engineer", "employee7@reliaquest.com"),
                new Employee("8", "Employee8", 8000, 25, "Engineer", "employee8@reliaquest.com"),
                new Employee("9", "Employee9", 9000, 25, "Engineer", "employee9@reliaquest.com"),
                new Employee("10", "Employee10", 10000, 25, "Engineer", "employee10@reliaquest.com"),
                new Employee("11", "Employee11", 11000, 25, "Engineer", "employee11@reliaquest.com"),
                new Employee("12", "Employee12", 12000, 25, "Engineer", "employee12@reliaquest.com"),
                new Employee("13", "Employee13", 13000, 25, "Engineer", "employee13@reliaquest.com"),
                new Employee("14", "Employee14", 14000, 25, "Engineer", "employee14@reliaquest.com"));

        return employees;
    }
}
