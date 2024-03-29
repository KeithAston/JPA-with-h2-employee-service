package com.ka.employeeservice.services;

import com.ka.employeeservice.exceptions.IllegalEmployeeArgumentException;
import com.ka.employeeservice.exceptions.ResourceNotFoundException;
import com.ka.employeeservice.models.Employee;
import com.ka.employeeservice.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;


@DataJpaTest
@SqlGroup({
        @Sql(value = "classpath:empty/reset.sql", executionPhase = BEFORE_TEST_METHOD),
        @Sql(value = "classpath:init/data.sql", executionPhase = BEFORE_TEST_METHOD)
})
public class EmployeeServiceIntegrationTest {

    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setup(){
        employee = new Employee();
        employee.setFirstName("TestJohn");
        employee.setLastName("TestSmith");
        employee.setAge(49);
        employee.setCompanyRole("Director");
        employeeService = new EmployeeService(employeeRepository);
    }

    @Test
    public void should_create_employee() throws IllegalEmployeeArgumentException {
        employeeService.createEmployee(employee);
        assertThat(this.employeeRepository.findAll()).hasSize(6);
    }

    @Test
    public void should_throw_exception_and_not_create_employee() {
        employee.setAge(66);
        assertThrows(IllegalEmployeeArgumentException.class, () -> employeeService.createEmployee(employee));
        assertThat(this.employeeRepository.findAll()).hasSize(5);
    }

    //find all employee tests

    @Test
    public void should_find_all_users() throws ResourceNotFoundException {
        List<Employee> employeesFound = employeeService.getAllEmployees();

        assertThat(employeesFound.size()).isEqualTo(5);
        assertThat(employeesFound.get(2).getLastName()).isEqualTo("Smith");
        assertThat(employeesFound.get(4).getAge()).isEqualTo(21);
    }


    //find one employee test

    @Test
    public void should_find_one_employee() throws ResourceNotFoundException {
        Employee foundEmployee = employeeService.findEmployee(1231);

        assertThat(foundEmployee.getFirstName()).isEqualTo("John");
        assertThat(foundEmployee.getLastName()).isEqualTo("Doe");
    }

    @Test
    public void should_throw_exception_not_finding_one_employee(){
        assertThrows(ResourceNotFoundException.class, () -> employeeService.findEmployee(199));
    }

    //delete employee tests
    @Test
    public void should_delete_employee() throws ResourceNotFoundException {
        var response = employeeService.deleteEmployee(1231);
        assertThat(response).isEqualTo("Successfully deleted employee 1231");
        assertThat(employeeRepository.findAll().size()).isEqualTo(4);
    }

    @Test
    public void should_throw_exception_for_missing_employee(){
        assertThrows(ResourceNotFoundException.class, () -> employeeService.deleteEmployee(12));
    }


}
