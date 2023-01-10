package ro.itschool.Booking.service;

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

    //POST
    public void createPerson(Person person) {
        checkMobileNumber(person);
        personRepository.save(person);

    }

    public Person savePerson(Person person) {
        checkMobileNumber(person);
        return personRepository.save(person);

    }

    //method to check if mobile number exists
    private void checkMobileNumber(Person person) {
        Optional<Object> savePerson = personRepository.getPersonByMobileNumber(person.getMobileNumber());
        if (savePerson.isPresent()) {
            throw new IllegalStateException(String.format("This mobile number %s is already used", person.getMobileNumber()));
        }
    }

    //UPDATE
    public void updatePerson(Long id, Person person) {
        Person updatePerson = personRepository.findById(id).orElseThrow(
                () -> new IllegalStateException(String.format("This id %s is not found", person.getPersonId())));
        checkMobileNumber(person);

        updatePerson.setLastName(person.getLastName());
        updatePerson.setFirstName(person.getFirstName());
        updatePerson.setMobileNumber(person.getMobileNumber());
        updatePerson.setEmail(person.getEmail());
        updatePerson.setCheckIn(person.getCheckIn());
        updatePerson.setCheckOut(person.getCheckOut());
    }

    //DELETE
    public void deletePerson(Long id) {
        if (id == null) {
            throw new IllegalStateException("This id " + id
                    + " is not found");
        } else
            personRepository.deleteById(id);
    }
}
