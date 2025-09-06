package com.reliaquest.api.service;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.dto.EmployeeRequest;
import java.util.List;

public interface IEmployeeApiClient {

    public List<Employee> getAllEmployees();

    public List<Employee> getEmployeesByNameSearch(String name);

    public Employee getEmployeeById(String id);

    public Integer getHighestSalaryOfEmployees();

    public List<String> getTopTenHighestEarningEmployeeNames();

    public Employee createEmployee(EmployeeRequest employeeRequest);

    public boolean deleteEmployeeById(String name);
}
