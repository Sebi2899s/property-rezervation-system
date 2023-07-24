package ro.itschool.Booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.entity.Property;
import ro.itschool.Booking.entity.Role;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.customException.IncorretNameException;
import ro.itschool.Booking.customException.MobileNumberException;
import ro.itschool.Booking.service.PersonService;
import ro.itschool.Booking.service.PropertyService;

import java.util.List;
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
    public ResponseEntity<List<Person>> getAllUsers(@RequestParam(defaultValue = "0") Integer pageNo,
                                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                                    @RequestParam(defaultValue = "id") String sortBy) {
        return new ResponseEntity<>(personService.getAllPersons(pageNo, pageSize, sortBy), HttpStatus.OK);
    }

    @GetMapping(value = "/get-properties")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Property>> getAllProperties(@RequestParam(defaultValue = "0") Integer pageNo,
                                                           @RequestParam(defaultValue = "10") Integer pageSize,
                                                           @RequestParam(defaultValue = "ASC") String sortBy) {
        return new ResponseEntity<>(propertyService.getAllProperties(pageNo, pageSize, sortBy), HttpStatus.OK);
    }

    @PostMapping(value = "/add-user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Person> addUser(@RequestBody Person person) throws MobileNumberException, IncorretNameException {
        checkEmailPersonExists(person);
        personService.savePerson(person);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }


    @PostMapping(value = "/add-property")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Property> addProperty(@RequestBody Property property) throws IncorretNameException {
        checkIfEmailPropertyExists(property);
        propertyService.createProperty(property);
        return new ResponseEntity<>(property, HttpStatus.OK);
    }


    @PutMapping(value = "/update-role/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Long> updateRole(@PathVariable Long id, @RequestBody Role roles) {
        return checkIfIdExistsAndSetRole(id, roles);
    }


    @DeleteMapping(value = "/remove-person/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Long> removePerson(@PathVariable Long id) throws IncorrectIdException {
        checkIfPersonIdExists(id);
        personService.deletePerson(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }


    @DeleteMapping(value = "/remove-property/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Long> removeProperty(@PathVariable Long id) throws IncorrectIdException {
        checkPropertyIdExists(id);
        personService.deletePerson(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    private void checkPropertyIdExists(Long id) throws IncorrectIdException {
        Optional<Property> findById = propertyService.findById(id);
        if (findById.isEmpty()) {
            throw new IncorrectIdException("This id " + id + " doesn't exists!");
        }
    }

    private void checkEmailPersonExists(Person person) throws IncorretNameException {
        Optional<Person> checkEmail = personService.findByEmail(person.getEmail());
        if (checkEmail.isPresent()) {
            throw new IncorretNameException("This email: " + person.getEmail() + " already exists!");
        }
    }

    private void checkIfEmailPropertyExists(Property property) throws IncorretNameException {
        Optional<Property> checkEmail = propertyService.findByPropertyEmail(property.getPropertyEmail());
        if (checkEmail.isPresent()) {
            throw new IncorretNameException("This email: " + property.getPropertyEmail() + " already exists!");
        }
    }

    private ResponseEntity<Long> checkIfIdExistsAndSetRole(Long id, Role roles) {
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
            return new ResponseEntity<>(id, HttpStatus.OK);
        } else
            return new ResponseEntity<>(id, HttpStatus.NOT_FOUND);
    }

    private void checkIfPersonIdExists(Long id) throws IncorrectIdException {
        Optional<Person> findById = personService.findById(id);
        if (findById.isEmpty()) {
            throw new IncorrectIdException("This id doesn't exists!");
        }
    }
}
