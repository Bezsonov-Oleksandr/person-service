package ait.cohort55.person.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "person")
public class Person implements Serializable {
    @Id
    private int id;
    @Setter
    private String name;
    private LocalDate birthDate;
    @Setter
    @Embedded
    private Address address;
}
