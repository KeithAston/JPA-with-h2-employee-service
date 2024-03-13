package com.ka.employeeservice.services;

import com.ka.employeeservice.exceptions.IllegalEmployeeArgumentException;
import com.ka.employeeservice.exceptions.ResourceNotFoundException;
import com.ka.employeeservice.models.Employee;
import com.ka.employeeservice.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Captor
    private ArgumentCaptor<Employee> employeeArgumentCaptor;

    private Employee employee;

    @BeforeEach
    public void setup(){
        employee = new Employee();
        employee.setFirstName("Dan");
        employee.setLastName("Smith");
        employee.setAge(49);
        employee.setCompanyRole("Director");
    }

    //create employee tests

    @Test
    public void should_create_employee() throws IllegalEmployeeArgumentException {
        employeeService.createEmployee(employee);
        verify(employeeRepository).save(employeeArgumentCaptor.capture());
        var response = employeeArgumentCaptor.getValue();

        then(response.getFirstName()).isEqualTo(employee.getFirstName());
        then(response.getLastName()).isEqualTo(employee.getLastName());
    }

    @Test
    public void should_reject_employee_role(){
        employee.setCompanyRole("NO_ROLE");
        assertThrows(IllegalEmployeeArgumentException.class, () -> employeeService.createEmployee(employee));
    }
    @Test
    public void should_reject_employee_age(){
        employee.setAge(90);
        assertThrows(IllegalEmployeeArgumentException.class, () -> employeeService.createEmployee(employee));
        employee.setAge(15);
        assertThrows(IllegalEmployeeArgumentException.class, () -> employeeService.createEmployee(employee));
    }

    @Test
    public void should_reject_employee_first_name_missing(){
        employee.setFirstName(" ");
        assertThrows(IllegalEmployeeArgumentException.class, () -> employeeService.createEmployee(employee));
    }

    @Test
    public void should_reject_employee_last_name_missing(){
        employee.setLastName(null);
        assertThrows(NullPointerException.class, () -> employeeService.createEmployee(employee));
    }

    //find all employee tests

    @Test
    public void should_find_all_employees() throws ResourceNotFoundException {
        List<Employee> employeesFound = new ArrayList<>();
        employeesFound.add(employee);
        when(employeeRepository.findAll()).thenReturn(employeesFound);
        var response = employeeService.getAllEmployees();

        assertThat(response.get(0)).isEqualTo(employee);
    }

    //find one employee tests

    @Test
    public void should_find_employee_by_id() throws ResourceNotFoundException {
        Optional<Employee> optionalEmployee = Optional.of(employee);
        when(employeeRepository.findById(any(Long.class))).thenReturn(optionalEmployee);

        assertThat(employeeService.findEmployee(123)).isEqualTo(employee);
    }

    @Test
    public void should_not_find_employee_by_id() {
        Optional<Employee> optionalEmployee = Optional.empty();
        when(employeeRepository.findById(any(Long.class))).thenReturn(optionalEmployee);

        assertThrows(ResourceNotFoundException.class, () -> employeeService.findEmployee(123));
    }

    //delete employee tests

    @Test
    public void should_delete_employee() throws ResourceNotFoundException {
        Optional<Employee> optionalEmployee = Optional.of(employee);
        when(employeeRepository.findById(any(Long.class))).thenReturn(optionalEmployee);

        assertThat(employeeService.deleteEmployee(123)).isEqualTo("Successfully deleted employee 123");
    }


}
