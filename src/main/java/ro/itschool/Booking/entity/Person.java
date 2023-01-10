package ro.itschool.Booking.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import ro.itschool.Booking.DtoEntity.PersonDTO;

import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personId;

    private String firstName;

    private String lastName;

    private String email;

    private String password;


    private String checkIn;

    private String checkOut;


    @Column(unique = true)
    private String mobileNumber;
    @JsonBackReference
    @ManyToOne
    @JoinTable(name = "reservations")
    @ToString.Exclude
    private Property property;


    public Person(Long personId, String firstName, String lastName, String email, String checkIn, String checkOut, String mobileNumber) {
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = Objects.requireNonNull(email);
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.mobileNumber = Objects.requireNonNull(mobileNumber);
    }


    public PersonDTO toDTO() {
        return new PersonDTO(personId, firstName, lastName, email, checkIn, checkOut);
    }


    public void assignProperty(Property property) {
        this.property = property;
    }

}
