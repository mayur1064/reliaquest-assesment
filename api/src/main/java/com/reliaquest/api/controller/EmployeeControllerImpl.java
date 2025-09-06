package com.reliaquest.api.controller;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.dto.EmployeeRequest;
import com.reliaquest.api.service.IEmployeeApiClient;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employeeimpl")
@RequiredArgsConstructor
public class EmployeeControllerImpl implements IEmployeeController<Employee, EmployeeRequest> {

    private final IEmployeeApiClient employeeApiClient;

    @Override
    @GetMapping()
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.status(HttpStatus.OK).body(employeeApiClient.getAllEmployees());
    }

    @Override
    @GetMapping("/search/{searchString}")
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable("searchString") String searchString) {
        return ResponseEntity.status(HttpStatus.OK).body(employeeApiClient.getEmployeesByNameSearch(searchString));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") String id) {
        Employee employee = employeeApiClient.getEmployeeById(id);
        if (Objects.isNull(employee)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(employee);
    }

    @Override
    @GetMapping("/max-salary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return ResponseEntity.status(HttpStatus.OK).body(employeeApiClient.getHighestSalaryOfEmployees());
    }

    @Override
    @GetMapping("/top-ten-employees")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return ResponseEntity.status(HttpStatus.OK).body(employeeApiClient.getTopTenHighestEarningEmployeeNames());
    }

    @Override
    @PostMapping()
    public ResponseEntity<Employee> createEmployee(@Valid EmployeeRequest employeeInput) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeApiClient.createEmployee(employeeInput));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable("id") String id) {
        if (employeeApiClient.deleteEmployeeById(id)) {
            return ResponseEntity.status(HttpStatus.OK).body("Employee Deleted Successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found with id - " + id);
    }
}
