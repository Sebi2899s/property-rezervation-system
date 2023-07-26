package ro.itschool.Booking.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ro.itschool.Booking.Dto.PersonDTO;
import ro.itschool.Booking.util.ClonePerson;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
//@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person implements UserDetails, ClonePerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personId;
    private String firstName;

    private String lastName;

    private String email;

    private String password;


    @Enumerated(EnumType.STRING)
    private Role role;


    @Column(unique = true)
    private String mobileNumber;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "property_id")
    @ToString.Exclude
    private Property property;

    @OneToMany(mappedBy = "person")
    @ToString.Exclude
    @JsonIgnore
    private List<Reservation> reservations;
    private boolean subscriber;


    public Person(Long personId, String firstName, String lastName, String email, String mobileNumber,boolean subscriber) {
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = Objects.requireNonNull(email);
        this.mobileNumber = Objects.requireNonNull(mobileNumber);
        this.subscriber=subscriber;
    }

    public Person(Person person) {

    }

    public PersonDTO toDTO() {
        return new PersonDTO(personId, firstName, lastName, email);
    }


    public void assignProperty(Property property) {
        this.property = property;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
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

    public static PersonBuilder builder() {
        return new PersonBuilder();
    }

    //implement Prototype Design Pattern that clone objects
    @Override
    public Person clone() {
        return new Person(this);
    }

    //implementing Builder Design Pattern for Person class
    public static class PersonBuilder {
        private Person person;

        private PersonBuilder() {
            this.person = new Person();
        }

        private PersonBuilder personId(Long id) {
            this.person.personId = id;
            return this;
        }

        public PersonBuilder firstName(String firstName) {
            this.person.firstName = firstName;
            return this;
        }

        public PersonBuilder lastName(String lastName) {
            this.person.lastName = lastName;
            return this;
        }

        public PersonBuilder email(String email) {
            this.person.email = email;
            return this;
        }

        public PersonBuilder password(String password) {
            this.person.password = password;
            return this;
        }

        public PersonBuilder role(Role role) {
            this.person.role = role;
            return this;
        }
        public PersonBuilder subscriber(boolean subscriber){
            this.person.subscriber=subscriber;
            return this;
        }

        public PersonBuilder mobileNumber(String mobileNumber) {
            this.person.mobileNumber = mobileNumber;
            return this;
        }

        public PersonBuilder property(Property property) {
            this.person.property = property;
            return this;
        }

        public Person build() {
            return this.person;
        }
    }

}
