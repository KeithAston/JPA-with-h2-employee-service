package com.ka.employeeservice.controllers;

import com.ka.employeeservice.exceptions.IllegalEmployeeArgumentException;
import com.ka.employeeservice.exceptions.ResourceNotFoundException;
import com.ka.employeeservice.models.Employee;
import com.ka.employeeservice.repository.EmployeeRepository;
import com.ka.employeeservice.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@DataJpaTest
@SqlGroup({
        @Sql(value = "classpath:empty/reset.sql", executionPhase = BEFORE_TEST_METHOD),
        @Sql(value = "classpath:init/data.sql", executionPhase = BEFORE_TEST_METHOD)
})
public class EmployeeControllerIntegrationTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private EmployeeController employeeController;
    private EmployeeService employeeService;
    private Employee employee;

    @BeforeEach
    public void setup(){
        employee = new Employee();
        employee.setFirstName("TestJohn");
        employee.setLastName("TestSmith");
        employee.setAge(49);
        employee.setCompanyRole("Director");

        employeeService = new EmployeeService(employeeRepository);
        employeeController = new EmployeeController(employeeService);
    }

    //create employee tests

    @Test
    public void should_create_employee() throws IllegalEmployeeArgumentException {
        employeeController.createEmployee(employee);
        assertThat(this.employeeRepository.findAll()).hasSize(6);
    }

    @Test
    public void should_throw_exception_and_not_create_employee() {
        employee.setAge(66);
        assertThrows(IllegalEmployeeArgumentException.class, () -> employeeController.createEmployee(employee));
        assertThat(this.employeeRepository.findAll()).hasSize(5);
    }

    //find all employee tests

    @Test
    public void should_find_all_employees() throws ResourceNotFoundException {
        var employeesFound = employeeController.getAllEmployees();
        assertThat(employeesFound.getBody().size()).isEqualTo(this.employeeRepository.findAll().size());

    }

    //find one employee tests

    @Test
    public void should_find_one_employee() throws ResourceNotFoundException {
        var response = employeeController.findEmployee(1231);
        assertThat(response.getBody().getFirstName()).isEqualTo("John");
        assertThat(response.getBody().getLastName()).isEqualTo("Doe");
    }

    @Test
    public void should_throw_exception_finding_one_employee(){
        assertThrows(ResourceNotFoundException.class, () -> employeeController.findEmployee(199));
    }

    //delete employee tests
    @Test
    public void should_delete_one_employee() throws ResourceNotFoundException {
        int id = 1231;
        Long longId = (long) id;
        assertThat(employeeRepository.findById(longId)).isNotEmpty();

        var response = employeeController.deleteEmployee(id);
        assertThat(employeeRepository.findAll().size()).isEqualTo(4);
        assertThat(employeeRepository.findById(longId)).isEmpty();
    }


}
