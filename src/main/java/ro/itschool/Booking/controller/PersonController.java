package ro.itschool.Booking.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.itschool.Booking.DtoEntity.PersonDTO;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.entity.Property;
import ro.itschool.Booking.exception.IncorrectIdException;
import ro.itschool.Booking.exception.IncorretNameException;
import ro.itschool.Booking.exception.MobileNumberException;
import ro.itschool.Booking.repository.PersonRepository;
import ro.itschool.Booking.repository.PropertyRepository;
import ro.itschool.Booking.service.PersonService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/bk/person")
public class PersonController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);
    //dependency injection with constructor
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;

    }

    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private PersonRepository personRepository;

    @GetMapping(value = "/get-all")
    public List<PersonDTO> personDTOS() {
        LOGGER.info("Getting all persons");
        return personService.getAllPersons().stream().map(Person::toDTO).toList();
    }

    @GetMapping(value = "/get-by-id/{id}")
    public Optional<PersonDTO> getPersonById(@PathVariable Long id) {
        LOGGER.info("Getting person by id");
        Optional<Person> idExists = personRepository.findById(id);
        if (idExists.isEmpty()) {
            throw new IllegalStateException("This id " + id + " is not found");
        }
        return idExists.map(Person::toDTO);
    }


    @PostMapping(value = "/save-person")
    public Person personSave(@RequestBody Person person) throws MobileNumberException, IncorretNameException {
        LOGGER.info("Saving a person");
        return personService.savePerson(person);
    }
    //make a rezervation with idperson and idproperty using update
    @PutMapping(value = "/{idPerson}/property-reservations/{idProperty}")
    public Person reservation(@PathVariable Long idPerson, @PathVariable Long idProperty, @RequestBody Person personCheck) {
        LOGGER.info("Updating a person to the property");
        Person person = personRepository.findById(idPerson).orElseThrow(() -> new IllegalStateException("This id" + idPerson + "was not found!"));
        Property property = propertyRepository.findById(idProperty).orElseThrow(() -> new IllegalStateException("This id" + idProperty + "was not found!"));
        person.assignProperty(property);
        person.setCheckIn(personCheck.getCheckIn());//add check-in
        person.setCheckOut(personCheck.getCheckOut());//add check-out
        return personRepository.save(person);
    }

    @PutMapping(value = "/update-person/{id}")
    public ResponseEntity personUpdate(@PathVariable Long id, @RequestBody Person person) throws MobileNumberException, IncorretNameException, IncorrectIdException {
        LOGGER.info("Updating a person using the id value");
        Optional<Person> checkId = personService.findById(id);
        if (checkId.isPresent()) {
            personService.updatePerson(id, person);
            return ResponseEntity.ok().body("The person with id " + id + " was updated");
        } else {
            return ResponseEntity.badRequest().body("This id was not found!");
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity deletePerson(@PathVariable Long id) throws IncorrectIdException {
        LOGGER.info("Deleting a person using the id value");
        Optional<Person> checkId = personService.findById(id);
        if (checkId.isEmpty()) {
            return ResponseEntity.badRequest().body("This id " + id + " was not found!");
        } else {
            personService.deletePerson(id);
            return ResponseEntity.ok().body("This person was deleted");
        }

    }
}
