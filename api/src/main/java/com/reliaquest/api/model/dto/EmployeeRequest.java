package com.reliaquest.api.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {
    @NotBlank(message = "Name must not be blank")
    private String name;

    @Min(value = 1, message = "Salary must be greater than 0")
    private Integer salary;

    @Min(value = 16, message = "Age must be at least 16")
    @Max(value = 75, message = "Age must be less than or equal to 75")
    private Integer age;

    @NotBlank(message = "Title must not be blank")
    private String title;
}
