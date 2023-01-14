package ro.itschool.Booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.repository.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/bk/user/login")
public class LoginControllerPerson {
    @Autowired
    private PersonRepository personRepository;

    Logger logger = Logger.getLogger(Person.class.getName());

    @PostMapping
    public void logMeIn(String email, String password) {
        Optional<Person> findByEmail = personRepository.findByEmail(email);
        if (findByEmail.isPresent())
            if (findByEmail.get().getPassword().equals(password)) {
                logger.log(Level.INFO, "LOGGED");
            } else {
                logger.log(Level.INFO, "NOT LOGGED IN");
            }
        else
            logger.log(Level.INFO, "NON-EXISTENT USER ");
    }

    @GetMapping("get-all-persons")
    public List<Person> getAllPerson() {
        return personRepository.findAll();
    }
}
