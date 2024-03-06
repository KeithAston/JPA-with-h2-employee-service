package com.ka.employeeservice.repository;

import com.ka.employeeservice.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
