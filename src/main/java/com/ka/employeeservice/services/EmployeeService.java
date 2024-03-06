package com.ka.employeeservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ka.employeeservice.exceptions.IllegalEmployeeArgumentException;
import com.ka.employeeservice.models.CompanyRole;
import com.ka.employeeservice.models.Employee;
import com.ka.employeeservice.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;


@Service
@AllArgsConstructor
@CommonsLog
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    @Transactional
    public String createEmployee(Employee employee) throws IllegalEmployeeArgumentException {
        validateEmployee(employee);
        employeeRepository.save(employee);
        return "Employee saved successfully";
    }

    private void validateEmployee(Employee employee) throws IllegalEmployeeArgumentException {
        if(!ObjectUtils.containsConstant(CompanyRole.values(),
                employee.getCompanyRole().toUpperCase(), true)
                || employee.getAge() > 65 || employee.getAge() < 16
                || employee.getFirstName().isBlank() || employee.getLastName().isBlank()){
            log.error("Employee arguments not valid : " + employee);
            throw new IllegalEmployeeArgumentException("Employee record is not valid");
        }
    }
}
