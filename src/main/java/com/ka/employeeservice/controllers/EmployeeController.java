package com.ka.employeeservice.controllers;

import com.ka.employeeservice.exceptions.IllegalEmployeeArgumentException;
import com.ka.employeeservice.exceptions.ResourceNotFoundException;
import com.ka.employeeservice.models.Employee;
import com.ka.employeeservice.services.EmployeeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/v1/employee")
@CommonsLog
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/create")
    public ResponseEntity<String> createEmployee(@Valid @RequestBody Employee employee) throws IllegalEmployeeArgumentException {
        return new ResponseEntity<>(employeeService.createEmployee(employee), HttpStatus.OK);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Employee>> getAllEmployees() throws ResourceNotFoundException {
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Employee> findEmployee(@PathVariable int id) throws ResourceNotFoundException {
        return new ResponseEntity<>(employeeService.findEmployee(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable int id) throws ResourceNotFoundException {
        return new ResponseEntity<>(employeeService.deleteEmployee(id), HttpStatus.OK);
    }




}
