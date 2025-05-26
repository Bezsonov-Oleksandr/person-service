package ait.cohort55.person.service;

import ait.cohort55.person.dto.ChildDto;
import ait.cohort55.person.dto.EmployeeDto;
import ait.cohort55.person.dto.PersonDto;
import ait.cohort55.person.model.Child;
import ait.cohort55.person.model.Employee;
import ait.cohort55.person.model.Person;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonModelDtoMapper {
    private final ModelMapper modelMapper;

    public PersonDto mapToDto(  Person person) {
        if (person instanceof Child) {
            return modelMapper.map(person, ChildDto.class);
        }
        if (person instanceof Employee) {
            return modelMapper.map(person, EmployeeDto.class);
        }
        return modelMapper.map(person, PersonDto.class);
    }

    public Person mapToModel(  PersonDto personDto) {
        if (personDto instanceof ChildDto) {
            return modelMapper.map(personDto, Child.class);
        }
        if (personDto instanceof EmployeeDto) {
            return modelMapper.map(personDto, Employee.class);
        }
        return modelMapper.map(personDto, Person.class);
    }
}
