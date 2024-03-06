package com.ka.employeeservice.services;

import com.ka.employeeservice.exceptions.IllegalEmployeeArgumentException;
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

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;


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


}
