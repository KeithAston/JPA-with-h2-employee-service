package com.ka.employeeservice.services;

import com.ka.employeeservice.exceptions.IllegalEmployeeArgumentException;
import com.ka.employeeservice.models.Employee;
import com.ka.employeeservice.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
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

}
