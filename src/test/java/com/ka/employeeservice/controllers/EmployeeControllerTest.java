package com.ka.employeeservice.controllers;

import com.ka.employeeservice.exceptions.IllegalEmployeeArgumentException;
import com.ka.employeeservice.models.Employee;
import com.ka.employeeservice.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeService;

    private Employee employee = new Employee();

    @BeforeEach
    public void setup(){
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setAge(42);
        employee.setCompanyRole("Director");
    }

    @Test
    public void should_create_employee_test() throws IllegalEmployeeArgumentException {
        when(employeeService.createEmployee(any(Employee.class))).thenReturn("Employee created successfully");
        var response = employeeController.createEmployee(employee);

        assert response.getStatusCode().is2xxSuccessful();
        assert Objects.equals(response.getBody(), "Employee created successfully");
    }

    @Test
    public void missing_details_should_not_create_employee_test() throws IllegalEmployeeArgumentException {
        employee.setFirstName("");
        employee.setLastName("");
        when(employeeService.createEmployee(any(Employee.class))).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> employeeController.createEmployee(employee));
    }

}
