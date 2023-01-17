package ro.itschool.Booking.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

//@Service
@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    //GET
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Optional<Person> getById(Long id) {
        Optional<Person> findById = personRepository.findById(id);
        if (findById.isEmpty()) {
            throw new IllegalStateException("This id " + id + " was not found!");
        } else {
            return personRepository.findById(id);
        }
    }


    //Post
    public Person savePerson(Person person) {
        checkMobileNumber(person);
        checkEmail(person);
        return personRepository.save(person);

    }

    //method to check if mobile number exists
    private void checkMobileNumber(Person person) {
        Optional<Object> savePerson = personRepository.getPersonByMobileNumber(person.getMobileNumber());
        if (savePerson.isPresent()) {
            throw new IllegalStateException(String.format("This mobile number %s is already used", person.getMobileNumber()));
        }
    }

    private void checkEmail(Person person) {
        Optional<Person> checkEmail = personRepository.findByEmail(person.getEmail());
        if (checkEmail.isPresent()) {
            if (!person.getEmail().contains("@")) {
                throw new IllegalStateException("Invalid email, must contain @ symbol");
            }
        }
    }

    //UPDATE
    public void updatePerson(Long id, Person person) {
        Person updatePerson = personRepository.findById(id).orElseThrow(
                () -> new IllegalStateException(String.format("This id %s is not found", person.getPersonId())));

        updatePerson.setLastName(person.getLastName());
        updatePerson.setFirstName(person.getFirstName());
        updatePerson.setMobileNumber(person.getMobileNumber());
        updatePerson.setEmail(person.getEmail());
        checkEmail(person);
        checkMobileNumber(person);
    }

    //DELETE
    public void deletePerson(Long id) {
        if (id == null) {
            throw new IllegalStateException("This id " + id
                    + " is not found");
        } else
            personRepository.deleteById(id);
    }

    public Optional<Person> findByEmail(String email) {
        return personRepository.findByEmail(email);
    }

    public Optional<Person> findById(Long id) {
        return personRepository.findById(id);
    }
}
