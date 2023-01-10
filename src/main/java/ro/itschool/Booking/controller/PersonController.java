package ro.itschool.Booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.itschool.Booking.DtoEntity.PersonDTO;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.entity.Property;
import ro.itschool.Booking.repository.PersonRepository;
import ro.itschool.Booking.repository.PropertyRepository;
import ro.itschool.Booking.service.PersonService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/bk/person")
public class PersonController {
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
        return personService.getAllPersons().stream().map(Person::toDTO).toList();
    }

    @GetMapping(value = "/get-by-id/{id}")
    public Optional<PersonDTO> getPersonById(@PathVariable Long id) {
        Optional<Person> idExists = personRepository.findById(id);
        if (idExists.isEmpty()) {
            throw new IllegalStateException("This id " + id + " is not found");
        }
        return idExists.map(Person::toDTO);
    }


    @PostMapping(value = "/save-person")
    public Person personSave(@RequestBody Person person) {
        return personService.savePerson(person);
    }

    @PutMapping(value = "/{idPerson}/property-reservations/{idProperty}")
    public Person reservation(@PathVariable Long idPerson, @PathVariable Long idProperty) {
        Person person= personRepository.findById(idPerson).orElseThrow(()->new IllegalStateException("This id"+idPerson+"was not found!"));
        Property property = propertyRepository.findById(idProperty).orElseThrow(()->new IllegalStateException("This id"+idProperty+"was not found!"));
        person.assignProperty(property);
        return personRepository.save(person);
    }

    @PutMapping(value = "/update-person/{id}")
    public void personUpdate(@PathVariable Long id, @RequestBody Person person) {
        personService.updatePerson(id, person);
    }

    @DeleteMapping(value = "/delete/{id}")
    public void deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
    }

}
