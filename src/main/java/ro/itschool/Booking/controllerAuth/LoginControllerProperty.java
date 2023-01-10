package ro.itschool.Booking.controllerAuth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.itschool.Booking.entity.Property;
import ro.itschool.Booking.repository.PropertyRepository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/bk/property/login")
public class LoginControllerProperty {
    private final PropertyRepository propertyRepository;

    public LoginControllerProperty(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    Logger logger = Logger.getLogger(Property.class.getName());

    @PostMapping
    public void logMeIn(String email, String password) {
        Optional<Property> findByEmail = propertyRepository.findByPropertyEmail(email);
        if (findByEmail.isPresent())
            if (findByEmail.get().getPropertyPassword().equals(password)) {
                logger.log(Level.INFO, "LOGGED");
            } else {
                logger.log(Level.INFO, "NOT LOGGED IN");
            }
        else
            logger.log(Level.INFO, "NON-EXISTENT USER ");
    }

    @GetMapping("get-all-persons")
    public List<Property> getAllPerson() {
        return propertyRepository.findAll();
    }
}
