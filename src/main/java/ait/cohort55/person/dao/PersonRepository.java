package ait.cohort55.person.dao;

import ait.cohort55.person.dto.CityPopulationDto;
import ait.cohort55.person.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public interface PersonRepository extends JpaRepository<Person, Integer> {

//    @Query("SELECT p FROM Person p WHERE p.name = ?1")
    Stream<Person> findByNameIgnoreCase(String name);

//    @Query("SELECT a FROM Person a WHERE a.address.city = :city")
    Stream<Person> findByAddressCityIgnoreCase(String city);

    Stream<Person> findByBirthDateBetween(LocalDate minDate, LocalDate maxDate);

    @Query("select "+
            "new ait.cohort55.person.dto.CityPopulationDto(p.address.city, count(p))"+
            " from Person p "+
            "group by p.address.city order by count(p) desc ")
    List<CityPopulationDto> getCitiesPopulation();
}
