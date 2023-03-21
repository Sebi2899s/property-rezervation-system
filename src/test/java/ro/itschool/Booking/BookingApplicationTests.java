package ro.itschool.Booking;

import com.mysql.cj.log.Log;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.parameters.P;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.repository.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@SpringBootTest
class BookingApplicationTests {
    @Autowired
    private PersonRepository personRepository;
    // TODO make tests for repo and endpoints

    private static final Logger logger = Logger.getLogger(BookingApplicationTests.class.getName());

    @Test
    void testSavePerson() {
        Person p = new Person();
        p.setFirstName("Mircea");
        p.setEmail("ss@gmail.com");
        personRepository.save(p);
        Assert.assertNotNull(p);
        Assert.assertEquals("Mircea", p.getFirstName());
        Assert.assertEquals("ss@gmail.com", p.getEmail());
    }

    @Test
    void testFindById() {
        Person p = new Person();
        p.setLastName("sss");
        personRepository.save(p);
        Optional<Person> getPersonById = personRepository.findById(p.getPersonId());

        Assert.assertEquals(p.getLastName(), getPersonById.get().getLastName());
        logger.info("Name from person that was saved... " + getPersonById.get().getLastName());
        logger.info("Name from person that was created ..." + p.getLastName());

    }

    @Test
    void testDeleteUser() {
        Person person = new Person();
        person.setFirstName("Sebi");
        person.setEmail("sss@gmail.com");
        personRepository.save(person);
        personRepository.delete(person);
        Optional<Person> findP = personRepository.findById(person.getPersonId());
        Assert.assertTrue(findP.isEmpty());
    }

    @Test
    void testFindAllPersons() {
        Person person = new Person();
        person.setLastName("Seee");

        var person2 = new Person();
        person2.setLastName("23");

        personRepository.save(person);
        personRepository.save(person2);

        List<Person> getAll = personRepository.findAll();

        Assert.assertEquals(2, getAll.size());
    }

    @Test
    void testFindByEmail() {
        Person person = new Person();
        person.setEmail("sef@gmail.com");

        personRepository.save(person);

        Optional<Person> findByEmail = personRepository.findByEmail(person.getEmail());

        Assert.assertEquals(person.getEmail(), findByEmail.get().getEmail());

    }
}
