package ro.itschool.Booking.service;

import org.springframework.stereotype.Service;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.exception.IncorrectIdException;
import ro.itschool.Booking.exception.IncorretNameException;
import ro.itschool.Booking.exception.MobileNumberException;
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

    public Optional<Person> getById(Long id) throws IncorrectIdException {
        Optional<Person> findById = personRepository.findById(id);
        if (findById.isEmpty()) {
            throw new IncorrectIdException("This id " + id + " was not found!");
        } else {
            return personRepository.findById(id);
        }
    }


    //Post
    public Person savePerson(Person person) throws MobileNumberException, IncorretNameException {


        checkMobileNumber(person);
        checkEmail(person);
        return personRepository.save(person);

    }

    //method to check if mobile number exists
    private void checkMobileNumber(Person person) throws MobileNumberException {
        Optional<Object> savePerson = personRepository.getPersonByMobileNumber(person.getMobileNumber());
        if (savePerson.isPresent()) {
            throw new MobileNumberException(String.format("This mobile number %s is already used", person.getMobileNumber()));
        }
    }

    private void checkEmail(Person person) throws IncorretNameException {
        Optional<Person> checkEmail = personRepository.findByEmail(person.getEmail());
        if (checkEmail.isPresent()) {
            if (!person.getEmail().contains("@")) {
                throw new IncorretNameException("Invalid email, must contain @ symbol");
            }
        }
    }

    //UPDATE
    public void updatePerson(Long id, Person person) throws IncorretNameException, MobileNumberException, IncorrectIdException {
        Person updatePerson = personRepository.findById(id).orElseThrow(
                () -> new IncorrectIdException(String.format("This id %s is not found", person.getPersonId())));

        updatePerson.setLastName(person.getLastName());
        updatePerson.setFirstName(person.getFirstName());
        updatePerson.setMobileNumber(person.getMobileNumber());
        updatePerson.setEmail(person.getEmail());
        checkEmail(person);
        checkMobileNumber(person);
    }

    //DELETE
    public void deletePerson(Long id) throws IncorrectIdException {
        if (id == null) {
            throw new IncorrectIdException("This id " + id
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
