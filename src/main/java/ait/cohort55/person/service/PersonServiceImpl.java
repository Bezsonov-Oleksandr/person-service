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
import java.util.List;

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
        } else if (personDto instanceof ChildDto) {
            personRepository.save(modelMapper.map(personDto, Child.class));
        } else if (personDto instanceof EmployeeDto) {
            personRepository.save(modelMapper.map(personDto, Employee.class));
        } else {
            personRepository.save(modelMapper.map(personDto, Person.class));
        }
    }

    @Override
    public PersonDto getPersonById(Integer id) {
        Person person = personRepository.findById(id).orElseThrow(NotFoundException::new);
        if (person instanceof Child) {
            return modelMapper.map(person, ChildDto.class);
        }
        if (person instanceof Employee) {
            return modelMapper.map(person, EmployeeDto.class);
        }
        return modelMapper.map(person, PersonDto.class);
    }

    @Transactional
    @Override
    public PersonDto deletePersonById(Integer id) {
        Person person = personRepository.findById(id).orElseThrow(NotFoundException::new);
        personRepository.delete(person);
        if (person instanceof Child) {
            return modelMapper.map(person, ChildDto.class);
        }
        if (person instanceof Employee) {
            return modelMapper.map(person, EmployeeDto.class);
        }
        return modelMapper.map(person, PersonDto.class);
    }

    @Transactional
    @Override
    public PersonDto updatePersonName(Integer id, String name) {
        Person person = personRepository.findById(id).orElseThrow(NotFoundException::new);
        person.setName(name);
        if (person instanceof Child) {
            return modelMapper.map(person, ChildDto.class);
        }
        if (person instanceof Employee) {
            return modelMapper.map(person, EmployeeDto.class);
        }
        return modelMapper.map(person, PersonDto.class);
    }

    @Transactional
    @Override
    public PersonDto updatePersonAddress(Integer id, AddressDto addressDto) {
        Person person = personRepository.findById(id).orElseThrow(NotFoundException::new);
        person.setAddress(modelMapper.map(addressDto, Address.class));
        if (person instanceof Child) {
            return modelMapper.map(person, ChildDto.class);
        }
        if (person instanceof Employee) {
            return modelMapper.map(person, EmployeeDto.class);
        }
        return modelMapper.map(person, PersonDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public PersonDto[] findPersonsByName(String name) {
        return personRepository.findByNameIgnoreCase(name)
                .map((person -> {
                    if (person instanceof Child) {
                        return modelMapper.map(person, ChildDto.class);
                    }
                    if (person instanceof Employee) {
                        return modelMapper.map(person, EmployeeDto.class);
                    }
                    return modelMapper.map(person, PersonDto.class);
                }))
                .toArray(PersonDto[]::new);
    }

    @Transactional(readOnly = true)
    @Override
    public PersonDto[] findPersonsByCity(String city) {
        return personRepository.findByAddressCityIgnoreCase(city)
                .map((person -> {
                    if (person instanceof Child) {
                        return modelMapper.map(person, ChildDto.class);
                    }
                    if (person instanceof Employee) {
                        return modelMapper.map(person, EmployeeDto.class);
                    }
                    return modelMapper.map(person, PersonDto.class);
                }))
                .toArray(PersonDto[]::new);
    }

    @Transactional(readOnly = true)
    @Override
    public PersonDto[] findPersonsBetweenAge(Integer minAge, Integer maxAge) {
        LocalDate minDate = LocalDate.now().minusYears(maxAge+1).plusDays(1);
        LocalDate maxDate = LocalDate.now().minusYears(minAge).plusDays(1);
        return personRepository.findByBirthDateBetween(minDate, maxDate)
                .map((person -> {
                    if (person instanceof Child) {
                        return modelMapper.map(person, ChildDto.class);
                    }
                    if (person instanceof Employee) {
                        return modelMapper.map(person, EmployeeDto.class);
                    }
                    return modelMapper.map(person, PersonDto.class);
                }))
                .toArray(PersonDto[]::new);
    }

    @Override
    public Iterable<CityPopulationDto> getCitiesPopulation() {
        return personRepository.getCitiesPopulation();
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
