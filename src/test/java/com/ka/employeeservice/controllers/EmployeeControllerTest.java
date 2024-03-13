package com.ka.employeeservice.controllers;

import com.ka.employeeservice.exceptions.IllegalEmployeeArgumentException;
import com.ka.employeeservice.exceptions.ResourceNotFoundException;
import com.ka.employeeservice.models.Employee;
import com.ka.employeeservice.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
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

    //create employee tests

    @Test
    public void should_create_employee() throws IllegalEmployeeArgumentException {
        when(employeeService.createEmployee(any(Employee.class))).thenReturn("Employee created successfully");
        var response = employeeController.createEmployee(employee);

        assert response.getStatusCode().is2xxSuccessful();
        assert Objects.equals(response.getBody(), "Employee created successfully");
    }

    @Test
    public void missing_details_should_not_create_employee() throws IllegalEmployeeArgumentException {
        employee.setFirstName("");
        employee.setLastName("");
        when(employeeService.createEmployee(any(Employee.class))).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> employeeController.createEmployee(employee));
    }

    //find all employee tests

    @Test
    public void should_find_all_employees() throws ResourceNotFoundException {
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);
        when(employeeService.getAllEmployees()).thenReturn(employeeList);
        var response = employeeController.getAllEmployees();
        assertThat(response.getBody().get(0)).isEqualTo(employee);
    }

    @Test
    public void should_throw_resource_not_found() throws ResourceNotFoundException {
        when(employeeService.getAllEmployees()).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> employeeController.getAllEmployees());
    }

    // find one employee tests

    @Test
    public void should_find_one_employee() throws ResourceNotFoundException {
        when(employeeService.findEmployee(any(Integer.class))).thenReturn(employee);
        assertThat(employeeController.findEmployee(123).getBody()).isEqualTo(employee);
    }

    @Test
    public void should_throw_exception_finding_employee() throws ResourceNotFoundException {
        when(employeeService.findEmployee(any(Integer.class))).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> employeeController.findEmployee(123));
    }

    //delete employee tests

    @Test
    public void should_delete_employee() throws ResourceNotFoundException {
        when(employeeService.deleteEmployee(any(Integer.class))).thenReturn("Successfully deleted employee 123");
        var response = employeeController.deleteEmployee(123);
        assertThat(response.getBody()).isEqualTo("Successfully deleted employee 123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }



}
