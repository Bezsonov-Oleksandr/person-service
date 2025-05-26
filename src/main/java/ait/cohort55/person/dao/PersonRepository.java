package ait.cohort55.person.dao;

import ait.cohort55.person.dto.CityPopulationDto;
import ait.cohort55.person.model.Child;
import ait.cohort55.person.model.Employee;
import ait.cohort55.person.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    Stream<Person> findByNameIgnoreCase(String name);

    Stream<Person> findByAddressCityIgnoreCase(String city);

    Stream<Person> findByBirthDateBetween(LocalDate minDate, LocalDate maxDate);

    @Query("select "+
            "new ait.cohort55.person.dto.CityPopulationDto(p.address.city, count(p))"+
            " from Person p "+
            "group by p.address.city order by count(p) desc ")
    List<CityPopulationDto> getCitiesPopulation();

    @Query("select c from Child c")
    Stream<Child> findAllChildren();

    @Query("select e from Employee e where e.salary between ?1 and ?2")
    Stream<Employee> findEmployeesBySalary(Integer minSalary, Integer maxSalary);
}
