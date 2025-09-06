package com.reliaquest.api.util;

import com.reliaquest.api.model.Employee;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MockDataHelper {

    public List<Employee> getEmployeeMockData() {
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
