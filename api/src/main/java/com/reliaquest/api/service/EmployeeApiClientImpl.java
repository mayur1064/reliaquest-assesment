package com.reliaquest.api.service;

import com.reliaquest.api.exception.EmployeeAPIException;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.dto.EmployeeRequest;
import com.reliaquest.api.model.dto.ServerResponse;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class EmployeeApiClientImpl implements IEmployeeApiClient {

    private static final String BASE_URL = "http://localhost:8112/api/v1/employee";
    private final RestTemplate restTemplate;

    public EmployeeApiClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retryable(
            retryFor = HttpClientErrorException.TooManyRequests.class,
            maxAttemptsExpression = "${retry.employee.max-attempts}",
            backoff =
                    @Backoff(
                            delayExpression = "${retry.employee.delay}",
                            multiplierExpression = "${retry.employee.multiplier}"))
    public List<Employee> getAllEmployees() {
        log.info("Fetching all employees from server");
        ServerResponse<List<Employee>> response = restTemplate
                .exchange(
                        BASE_URL,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<ServerResponse<List<Employee>>>() {})
                .getBody();
        return Objects.requireNonNull(response).data();
    }

    @Retryable(
            retryFor = HttpClientErrorException.TooManyRequests.class,
            maxAttemptsExpression = "${retry.employee.max-attempts}",
            backoff =
                    @Backoff(
                            delayExpression = "${retry.employee.delay}",
                            multiplierExpression = "${retry.employee.multiplier}"))
    public List<Employee> getEmployeesByNameSearch(String name) {
        log.info("Finding employees by name containing '{}'", name);
        return getAllEmployees().stream()
                .filter(e -> e.getEmployee_name().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    @Retryable(
            retryFor = HttpClientErrorException.TooManyRequests.class,
            maxAttemptsExpression = "${retry.employee.max-attempts}",
            backoff =
                    @Backoff(
                            delayExpression = "${retry.employee.delay}",
                            multiplierExpression = "${retry.employee.multiplier}"))
    public Employee getEmployeeById(String id) {
        ResponseEntity<ServerResponse<Employee>> response = restTemplate.exchange(
                BASE_URL + "/" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ServerResponse<Employee>>() {});

        if (response.getBody() != null && response.getBody().data() != null) {
            return response.getBody().data();
        } else {
            return null;
        }
    }

    public Integer getHighestSalaryOfEmployees() {
        log.info("Finding highest salary of employees");
        return getAllEmployees().stream()
                .map(Employee::getEmployee_salary)
                .max(Integer::compareTo)
                .orElseThrow(() -> new EmployeeAPIException("No employees found"));
    }

    public List<String> getTopTenHighestEarningEmployeeNames() {
        log.info("Finding top 10 highest earning employees");
        return getAllEmployees().stream()
                .sorted((a, b) -> Integer.compare(b.getEmployee_salary(), a.getEmployee_salary()))
                .limit(10)
                .map(Employee::getEmployee_name)
                .toList();
    }

    @Retryable(
            retryFor = HttpClientErrorException.TooManyRequests.class,
            maxAttemptsExpression = "${retry.employee.max-attempts}",
            backoff =
                    @Backoff(
                            delayExpression = "${retry.employee.delay}",
                            multiplierExpression = "${retry.employee.multiplier}"))
    public Employee createEmployee(EmployeeRequest employeeRequest) {
        log.info("Creating new employee with name - {}", employeeRequest.getName());
        HttpEntity<EmployeeRequest> request = new HttpEntity<>(employeeRequest);
        ResponseEntity<ServerResponse<Employee>> response = restTemplate.exchange(
                BASE_URL, HttpMethod.POST, request, new ParameterizedTypeReference<ServerResponse<Employee>>() {});
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new EmployeeAPIException("Employee Creation Failed");
        }
        return Objects.requireNonNull(response.getBody()).data();
    }

    @Retryable(
            retryFor = HttpClientErrorException.TooManyRequests.class,
            maxAttemptsExpression = "${retry.employee.max-attempts}",
            backoff =
                    @Backoff(
                            delayExpression = "${retry.employee.delay}",
                            multiplierExpression = "${retry.employee.multiplier}"))
    public boolean deleteEmployeeById(String id) {
        log.info("Finding the employee using ID {} to get name ", id);
        Employee employee = getEmployeeById(id);
        if (Objects.isNull(employee)) {
            throw new EmployeeAPIException("Deletion failed. Employee Not found");
        }
        log.info("Employee Id found {}. Name - {} ", id, employee.getEmployee_name());
        log.info("Deleting employee with name={}", employee.getEmployee_name());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(Map.of("name", employee.getEmployee_name()), headers);

        ServerResponse<Boolean> response = restTemplate
                .exchange(
                        BASE_URL,
                        HttpMethod.DELETE,
                        entity,
                        new ParameterizedTypeReference<ServerResponse<Boolean>>() {})
                .getBody();

        return Objects.requireNonNull(response).data();
    }
}
