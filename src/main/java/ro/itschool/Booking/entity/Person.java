package ro.itschool.Booking.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ro.itschool.Booking.DtoEntity.PersonDTO;

import java.util.Collection;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personId;

    private String firstName;

    private String lastName;

    private String email;

    private String password;


    private String checkIn;

    private String checkOut;

    @Enumerated(EnumType.STRING)
    private Role role;


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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
