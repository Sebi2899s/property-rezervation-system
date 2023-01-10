package ro.itschool.Booking.DtoEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.itschool.Booking.entity.Person;

import java.util.Objects;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonDTO {
    private Long personId;

    private String firstName;

    private String lastName;

    private String email;
    private String checkIn;

    private String checkOut;


    private String mobileNumber;

    public PersonDTO(Long personId, String firstName, String lastName, String email, String checkIn, String checkOut) {
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public Person toEntity() {
        return new Person(personId, firstName, lastName, email, checkIn, checkOut, mobileNumber);
    }
}
