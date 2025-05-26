package ait.cohort55.person.service;

import ait.cohort55.person.dao.PersonRepository;
import ait.cohort55.person.dto.*;
import ait.cohort55.person.dto.exception.ConflictException;
import ait.cohort55.person.dto.exception.NotFoundException;
import ait.cohort55.person.model.Address;
import ait.cohort55.person.model.Child;
import ait.cohort55.person.model.Employee;
import ait.cohort55.person.model.Person;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor

public class PersonServiceImpl implements PersonService, CommandLineRunner {
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;
    private final PersonModelDtoMapper mapper;

    @Transactional
    @Override
    public void addPerson(PersonDto personDto) {
        if (personRepository.existsById(personDto.getId())) {
            throw new ConflictException("Person with id " + personDto.getId() + " already exists");
        }
        personRepository.save(mapper.mapToModel(personDto));
    }

    @Override
    public PersonDto getPersonById(Integer id) {
        Person person = personRepository.findById(id).orElseThrow(NotFoundException::new);
        return mapper.mapToDto(person);
    }

    @Transactional
    @Override
    public PersonDto deletePersonById(Integer id) {
        Person person = personRepository.findById(id).orElseThrow(NotFoundException::new);
        personRepository.delete(person);
        return mapper.mapToDto(person);
    }

    @Transactional
    @Override
    public PersonDto updatePersonName(Integer id, String name) {
        Person person = personRepository.findById(id).orElseThrow(NotFoundException::new);
        person.setName(name);
        return mapper.mapToDto(person);
    }

    @Transactional
    @Override
    public PersonDto updatePersonAddress(Integer id, AddressDto addressDto) {
        Person person = personRepository.findById(id).orElseThrow(NotFoundException::new);
        person.setAddress(modelMapper.map(addressDto, Address.class));
        return mapper.mapToDto(person);
    }

    @Transactional(readOnly = true)
    @Override
    public PersonDto[] findPersonsByName(String name) {
        return personRepository.findByNameIgnoreCase(name)
                .map(mapper::mapToDto)
                .toArray(PersonDto[]::new);
    }

    @Transactional(readOnly = true)
    @Override
    public PersonDto[] findPersonsByCity(String city) {
        return personRepository.findByAddressCityIgnoreCase(city)
                .map(mapper::mapToDto)
                .toArray(PersonDto[]::new);
    }

    @Transactional(readOnly = true)
    @Override
    public PersonDto[] findPersonsBetweenAge(Integer minAge, Integer maxAge) {
        LocalDate minDate = LocalDate.now().minusYears(maxAge+1).plusDays(1);
        LocalDate maxDate = LocalDate.now().minusYears(minAge).plusDays(1);
        return personRepository.findByBirthDateBetween(minDate, maxDate)
                .map(mapper::mapToDto)
                .toArray(PersonDto[]::new);
    }

    @Override
    public Iterable<CityPopulationDto> getCitiesPopulation() {
        return personRepository.getCitiesPopulation();
    }

    @Transactional(readOnly = true)
    @Override
    public ChildDto[] findAllChildren() {
        return personRepository.findAllChildren()
                .map(mapper::mapToDto)
                .toArray(ChildDto[]::new);
    }

    @Transactional(readOnly = true)
    @Override
    public EmployeeDto[] findAllEmployeesBySalary(Integer minSalary, Integer maxSalary) {
        return personRepository.findEmployeesBySalary(minSalary, maxSalary)
                .map(mapper::mapToDto)
                .toArray(EmployeeDto[]::new);
    }


    @Override
    public void run(String... args) throws Exception {
        if (personRepository.count() == 0) {
            Person person = new Person(1000,"John",
                    LocalDate.of(1985,3,11),
                    new Address("Sumy","Simferop",15));
            Child child = new Child(1001, "Peter",
                    LocalDate.of(2019,7,5),
                    new Address("Sumy", "Hauptstr", 10),
                    "Kita");
            Employee employee = new Employee(1002, "Mary",
                    LocalDate.of(1995, 11, 25),
                    new Address("Sumy", "Hauptstr", 10),
                    "Coogle", 8000);
            personRepository.save(person);
            personRepository.save(child);
            personRepository.save(employee);
        }
    }
}
