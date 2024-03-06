package com.ka.employeeservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="EMPLOYEES")
@Data
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "FIRSTNAME", nullable = false)
    @NotEmpty
    private String firstName;

    @Column(name = "LASTNAME", nullable = false)
    @NotEmpty
    private String lastName;

    @Column(name = "AGE", nullable = false)
    @Min(16)
    @Max(65)
    private int age;

    @Column(name = "COMPANYPOSITION", nullable = false)
    @NotEmpty
    private String companyRole;

}
