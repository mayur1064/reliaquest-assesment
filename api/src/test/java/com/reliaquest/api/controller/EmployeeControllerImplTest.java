package com.reliaquest.api.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.dto.EmployeeRequest;
import com.reliaquest.api.service.IEmployeeApiClient;
import com.reliaquest.api.util.MockDataHelper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EmployeeControllerImpl.class)
public class EmployeeControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IEmployeeApiClient employeeApiClient;

    private MockDataHelper mockDataHelper;

    @BeforeEach
    void setup() {
        mockDataHelper = new MockDataHelper();
    }

    @Test
    void testGetAllEmployees() throws Exception {
        List<Employee> employees = mockDataHelper.getEmployeeMockData();
        when(employeeApiClient.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/api/v1/employeeimpl"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].employee_name").value("Employee1"))
                .andExpect(jsonPath("$", hasSize(14)));
    }

    @Test
    void testGetEmployeesByNameSearch() throws Exception {
        when(employeeApiClient.getEmployeesByNameSearch("Emp")).thenReturn(mockDataHelper.getEmployeeMockData());

        mockMvc.perform(get("/api/v1/employeeimpl/search/Emp"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employee_name").value("Employee1"))
                .andExpect(jsonPath("$", hasSize(14)));
    }

    @Test
    void testGetEmployeeByIdFound() throws Exception {
        Employee employee = new Employee("1", "Employee1", 1000, 25, "Engineer", "employee1@reliaquest.com");
        when(employeeApiClient.getEmployeeById("1")).thenReturn(employee);

        mockMvc.perform(get("/api/v1/employeeimpl/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employee_name").value("Employee1"));
    }

    @Test
    void testGetEmployeeByIdNotFound() throws Exception {
        when(employeeApiClient.getEmployeeById("100")).thenReturn(null);

        mockMvc.perform(get("/api/v1/employeeimpl/100")).andExpect(status().isNotFound());
    }

    @Test
    void testGetHighestSalaryOfEmployees() throws Exception {
        when(employeeApiClient.getHighestSalaryOfEmployees()).thenReturn(90000);

        mockMvc.perform(get("/api/v1/employeeimpl/max-salary"))
                .andExpect(status().isOk())
                .andExpect(content().string("90000"));
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames() throws Exception {
        List<String> employeeNames = mockDataHelper.getEmployeeMockData().stream()
                .map(Employee::getEmployee_name)
                .toList();
        when(employeeApiClient.getTopTenHighestEarningEmployeeNames()).thenReturn(employeeNames);

        mockMvc.perform(get("/api/v1/employeeimpl/top-ten-employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Employee1"));
    }

    @Test
    void testCreateEmployee() throws Exception {
        EmployeeRequest request = new EmployeeRequest("Employee1", 50000, 25, "HR");
        Employee response = new Employee("1", "Employee1", 50000, 25, "HR", "employee1@reliaquest.com");
        when(employeeApiClient.createEmployee(any(EmployeeRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/employeeimpl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Employee1\",\"salary\":50000,\"age\":25,\"title\":\"HR\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.employee_age").value("25"))
                .andExpect(jsonPath("$.employee_salary").value("50000"))
                .andExpect(jsonPath("$.employee_email").value("employee1@reliaquest.com"))
                .andExpect(jsonPath("$.employee_name").value("Employee1"));
    }

    @Test
    void testDeleteEmployeeByIdFound() throws Exception {
        when(employeeApiClient.deleteEmployeeById("1")).thenReturn(true);

        mockMvc.perform(delete("/api/v1/employeeimpl/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee Deleted Successfully"));
    }

    @Test
    void testDeleteEmployeeByIdNotFound() throws Exception {
        when(employeeApiClient.deleteEmployeeById("100")).thenReturn(false);

        mockMvc.perform(delete("/api/v1/employeeimpl/100"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Employee not found with id - 100"));
    }
}
