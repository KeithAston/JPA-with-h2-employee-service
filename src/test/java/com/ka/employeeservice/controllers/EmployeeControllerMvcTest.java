package com.ka.employeeservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ka.employeeservice.models.Employee;
import com.ka.employeeservice.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerMvcTest {

    @Autowired
    private MockMvc mvc;

    @InjectMocks
    private EmployeeController employeeController;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Captor
    private ArgumentCaptor<Employee> employeeArgumentCaptor;

    private Employee employee = new Employee();

    @BeforeEach
    public void setup(){
        employeeController = new EmployeeController(employeeService);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setAge(42);
        employee.setCompanyRole("Director");

        mvc = MockMvcBuilders
                .standaloneSetup(employeeController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @Test
    public void should_create_employee() throws Exception {
        when(employeeService.createEmployee(any(Employee.class))).thenReturn("Success");


        mvc.perform(MockMvcRequestBuilders
                .post("/v1/employee/create")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
            .andDo(print())
            .andExpectAll(
                    MockMvcResultMatchers.status().isOk()
            );

        verify(employeeService).createEmployee(employeeArgumentCaptor.capture());
        var response = employeeArgumentCaptor.getValue();
        then(response).isEqualTo(employee);

    }

    //can add more exception catcher tests like below

    @Test
    public void should_catch_thrown_null_pointer_exception() throws Exception {
        when(employeeService.createEmployee(any(Employee.class))).thenThrow(NullPointerException.class);

        mvc.perform(MockMvcRequestBuilders
                .post("/v1/employee/create")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andDo(print())
                .andExpectAll(
                        MockMvcResultMatchers.status().is5xxServerError(),
                        MockMvcResultMatchers.content().string("Internal system error, please contact support")
                );

    }

    @Test
    public void should_catch_thrown_illegal_state_exception() throws Exception {
        when(employeeService.createEmployee(any(Employee.class))).thenThrow(IllegalStateException.class);

        mvc.perform(MockMvcRequestBuilders
                .post("/v1/employee/create")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andDo(print())
                .andExpectAll(
                        MockMvcResultMatchers.status().is5xxServerError(),
                        MockMvcResultMatchers.content().string("Internal system error, please contact support")
                );

    }

    @Test
    public void should_catch_thrown_illegal_argument_exception() throws Exception {
        when(employeeService.createEmployee(any(Employee.class))).thenThrow(IllegalArgumentException.class);

        mvc.perform(MockMvcRequestBuilders
                .post("/v1/employee/create")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andDo(print())
                .andExpectAll(
                        MockMvcResultMatchers.status().is5xxServerError(),
                        MockMvcResultMatchers.content().string("Internal system error, please contact support")
                );

    }

}
