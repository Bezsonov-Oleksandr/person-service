package ait.cohort55.person.dto;

import lombok.Getter;

@Getter
public class EmployeeDto extends PersonDto {
    private String company;
    private int salary;
}
