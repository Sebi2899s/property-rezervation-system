package ro.itschool.Booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.entity.Property;
import ro.itschool.Booking.entity.Role;
import ro.itschool.Booking.exception.IncorrectIdException;
import ro.itschool.Booking.exception.IncorretNameException;
import ro.itschool.Booking.exception.MobileNumberException;
import ro.itschool.Booking.service.PersonService;
import ro.itschool.Booking.service.PropertyService;

import java.util.Optional;

@RestController
@EnableWebSecurity
@RequestMapping(value = "admin")

@RequiredArgsConstructor
public class AdminController {
    private final PersonService personService;
    private final PropertyService propertyService;

    @GetMapping(value = "/get-users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity getAllUsers() {
        return new ResponseEntity<>(personService.getAllPersons(), HttpStatus.OK);
    }

    @GetMapping(value = "/get-properties")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity getAllProperties() {
        return new ResponseEntity<>(propertyService.getAllProperties(), HttpStatus.OK);
    }

    @PostMapping(value = "/add-user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity addUser(@RequestBody Person person) throws MobileNumberException, IncorretNameException {
        Optional<Person> checkEmail = personService.findByEmail(person.getEmail());
        if (checkEmail.isEmpty()) {
            personService.savePerson(person);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("This email is already used");
        }
    }

    @PostMapping(value = "/add-property")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity addProperty(@RequestBody Property property) {
        Optional<Property> checkEmail = propertyService.findByPropertyEmail(property.getPropertyEmail());
        if (checkEmail.isEmpty()) {
            propertyService.createProperty(property);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("This email is already used");
        }
    }

    @PutMapping(value = "/update-role/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity updateRole(@PathVariable Long id, @RequestBody Role roles) {
        Optional<Person> optionalPerson = personService.findById(id);
        if (optionalPerson.isPresent()) {
            optionalPerson.ifPresent(user -> {
                user.setRole(roles);
                try {
                    personService.savePerson(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return ResponseEntity.ok().build();
        } else
            return ResponseEntity.badRequest().build();
    }

    @DeleteMapping(value = "/remove-person/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity removePerson(@PathVariable Long id) throws IncorrectIdException {
        Optional<Person> findById = personService.findById(id);
        if (findById.isPresent()) {
            personService.deletePerson(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("This id " + id + " was not found!");
        }
    }

    @DeleteMapping(value = "/remove-property/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity removeProperty(@PathVariable Long id) throws IncorrectIdException {
        Optional<Property> findById = propertyService.findById(id);
        if (findById.isPresent()) {
            personService.deletePerson(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("This id " + id + " was not found!");
        }

    }
}
