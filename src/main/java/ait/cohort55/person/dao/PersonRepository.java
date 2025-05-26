package ait.cohort55.person.dao;

import ait.cohort55.person.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface PersonRepository extends JpaRepository<Person, Integer> {

//    @Query("SELECT p FROM Person AS p WHERE p.name = :name")
    Set<Person> findByNameIgnoreCase(String name);

//    @Query("SELECT a FROM Person a WHERE a.address.city = :city")
    List<Person> findByAddressCityIgnoreCase(String city);

    List<Person> findByBirthDateBetween(LocalDate minDate, LocalDate maxDate);
}
