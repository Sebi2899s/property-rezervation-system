package ro.itschool.Booking.convertorDTO;

import org.springframework.stereotype.Service;
import ro.itschool.Booking.DtoEntity.PersonDTO;
import ro.itschool.Booking.entity.Person;

import java.util.List;

@Service
public class PersonConvertor {


    public PersonDTO entityToDto(Person person) {
        //convert entity to DTO
        PersonDTO personDTO = new PersonDTO();
        personDTO.setPersonId(person.getPersonId());
        personDTO.setFirstName(person.getFirstName());
        personDTO.setLastName(person.getLastName());
        personDTO.setEmail(person.getEmail());
        personDTO.setCheckIn(person.getCheckIn());
        personDTO.setCheckOut(person.getCheckOut());
        personDTO.setMobileNumber(person.getMobileNumber());

        return personDTO;
    }

    public List<PersonDTO> entityToDto(List<Person> person) {

        return person.stream().map(this::entityToDto).toList();
    }


    public Person dtoToEntity(PersonDTO personDTO) {
        //convert DTO to entity
        Person person = new Person();
        person.setPersonId(personDTO.getPersonId());
        person.setFirstName(personDTO.getFirstName());
        person.setLastName(personDTO.getLastName());
        person.setEmail(personDTO.getEmail());
        person.setCheckIn(personDTO.getCheckIn());
        person.setCheckOut(personDTO.getCheckOut());
        person.setMobileNumber(personDTO.getMobileNumber());
        return person;
    }

    public List<Person> dtoToEntity(List<PersonDTO> personDTO) {

        return personDTO.stream().map(this::dtoToEntity).toList();

    }
}
