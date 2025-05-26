package ait.cohort55.person.service;

import ait.cohort55.person.dto.*;

public interface PersonService {
    void addPerson(PersonDto personDto);
    PersonDto getPersonById(Integer id);
    PersonDto deletePersonById(Integer id);
    PersonDto updatePersonName(Integer id, String name);
    PersonDto updatePersonAddress(Integer id, AddressDto addressDto);
    PersonDto[] findPersonsByName(String name);
    PersonDto[] findPersonsByCity(String city);
    PersonDto[] findPersonsBetweenAge(Integer minAge, Integer maxAge);
    Iterable<CityPopulationDto> getCitiesPopulation();
    ChildDto[] findAllChildren();
    EmployeeDto[] findAllEmployeesBySalary(Integer minSalary, Integer maxSalary);
}
