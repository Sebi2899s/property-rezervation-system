package ro.itschool.Booking.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.itschool.Booking.DtoEntity.PersonDTO;
import ro.itschool.Booking.convertorDTO.PersonConvertor;
import ro.itschool.Booking.customException.PersonNotFoundException;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.entity.Property;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.customException.IncorretNameException;
import ro.itschool.Booking.customException.MobileNumberException;
import ro.itschool.Booking.repository.PersonRepository;
import ro.itschool.Booking.repository.PropertyRepository;
import ro.itschool.Booking.service.PersonService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/person")
public class PersonController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);
    @Autowired
    private PersonService personService;
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private PersonRepository personRepository;


    //---------------------------------------------------------------------------------------------------------------------
    @GetMapping(value = "/get-all")
    public List<PersonDTO> personDTOS(@RequestParam(defaultValue = "0") Integer pageNo,
                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                      @RequestParam(defaultValue = "id") String sortBy) {
        LOGGER.info("Getting all persons");
        return personService.getAllPersons(pageNo, pageSize, sortBy).stream().map(Person::toDTO).toList();
    }


    //---------------------------------------------------------------------------------------------------------------------
    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<PersonDTO>> getPersonById(@PathVariable Long id) {
        LOGGER.info("Getting person by id");
        return new ResponseEntity<>(checkIfIdExistsConvertToDto(id), HttpStatus.OK);
    }




    //---------------------------------------------------------------------------------------------------------------------
    @GetMapping(value = "/excel")
    public void generateExcelReport(HttpServletResponse response) throws IOException {


        response.setContentType("application");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=persons.xlsx";

        response.setHeader(headerKey, headerValue);
        personService.generateExcel(response);
    }

//---------------------------------------------------------------------------------------------------------------------

    @PostMapping(value = "/save")
    public ResponseEntity<PersonDTO> personSave(@RequestBody Person person) throws PersonNotFoundException {
        LOGGER.info("Saving a person");
        PersonConvertor personConvertor = new PersonConvertor();
        personService.createOrUpdatePerson(person, null);
        return new ResponseEntity<>(personConvertor.entityToDto(person), HttpStatus.OK);
    }


//---------------------------------------------------------------------------------------------------------------------
    //make a rezervation with idperson and idproperty using update

    @PutMapping(value = "/{idPerson}/reservations/{idProperty}")
    public ResponseEntity<Person> reservation(@PathVariable Long idPerson, @PathVariable Long idProperty, @RequestBody Person personCheck) {
        LOGGER.info("Updating a person to the property");
        Person person = reservationWithIdPersonIdProperty(idPerson, idProperty, personCheck);
        return new ResponseEntity<>(personRepository.save(person), HttpStatus.OK);
    }
//---------------------------------------------------------------------------------------------------------------------

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<PersonDTO> personUpdate(@PathVariable Long id, @RequestBody Person person) throws IncorrectIdException, PersonNotFoundException {
        LOGGER.info("Updating a person using the id value");
        checkIFIdExists(id);
        personService.createOrUpdatePerson(person, id);
        PersonConvertor personConvertor = new PersonConvertor();
        return new ResponseEntity<>(personConvertor.entityToDto(person), HttpStatus.OK);
    }


    //---------------------------------------------------------------------------------------------------------------------
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Long> deletePerson(@PathVariable Long id) throws IncorrectIdException {
        LOGGER.info("Deleting a person using the id value");
        checkIFIdExists(id);
        return new ResponseEntity<>(id, HttpStatus.OK);

    }


//---------------------------------------------------------------------------------------------------------------------

    private Optional<PersonDTO> checkIfIdExistsConvertToDto(Long id) {
        Optional<Person> idExists = personRepository.findById(id);
        if (idExists.isEmpty()) {
            throw new IllegalStateException("This id " + id + " is not found");
        }
        return idExists.map(Person::toDTO);
    }

//---------------------------------------------------------------------------------------------------------------------

    private void checkIFIdExists(Long id) throws IncorrectIdException {
        Optional<Person> checkId = personService.findById(id);
        if (checkId.isEmpty()) {
            throw new IncorrectIdException("This id doesn't exists!");
        }
    }


    //---------------------------------------------------------------------------------------------------------------------
    private Person reservationWithIdPersonIdProperty(Long idPerson, Long idProperty, Person personCheck) {
        Person person = personRepository.findById(idPerson).orElseThrow(() -> new IllegalStateException("This id" + idPerson + "was not found!"));
        Property property = propertyRepository.findById(idProperty).orElseThrow(() -> new IllegalStateException("This id" + idProperty + "was not found!"));
        person.assignProperty(property);
        return person;
    }


}
