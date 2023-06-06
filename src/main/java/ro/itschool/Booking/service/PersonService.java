package ro.itschool.Booking.service;

import jakarta.annotation.Nullable;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import ro.itschool.Booking.DtoEntity.PersonDTO;
import ro.itschool.Booking.customException.PersonNotFoundException;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.customException.IncorretNameException;
import ro.itschool.Booking.customException.MobileNumberException;
import ro.itschool.Booking.repository.PersonRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    private final ModelMapper mapper;

    public PersonService(PersonRepository personRepository, ModelMapper mapper) {
        this.personRepository = personRepository;
        this.mapper = mapper;
    }

    //GET
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Optional<PersonDTO> getById(Long id) throws IncorrectIdException {
        Optional<Person> person = personRepository.findById(id);
        PersonDTO personDto = mapper.map(person, PersonDTO.class);
        if (person.isEmpty()) {
            throw new IncorrectIdException("This id " + id + " was not found!");
        } else {
            return Optional.ofNullable(personDto);
        }
    }

    public Person getPersonOrThrow(Long id) throws PersonNotFoundException {
        Optional<Person> person = personRepository.findById(id);
        return person.orElseThrow(() -> new PersonNotFoundException("this person is not found"));
    }


    //Post
    public Person savePerson(Person person) throws MobileNumberException, IncorretNameException {
        checkEmailExists(person);
        checkToHaveSpecificCharacterInEmail(person);
        checkMobileNumber(person);
        return personRepository.save(person);

    }

    public Person createOrUpdatePerson(@NotNull Person person_p, @Nullable Long id) throws PersonNotFoundException {
        Person person;
        String sMessage = null;
        //update case
        if (id != null) {
            person = getPersonOrThrow(id);
            sMessage = "Its an error with updating a person";

        } else {
            person_p.setPersonId(null);
            sMessage = "Its an error with saving a person";
        }
        try {

            person = savePerson(person_p);
        } catch (Exception e) {
            throw new RuntimeException(sMessage);
        }
        return person;
    }

    private void checkEmailExists(Person person) throws IncorretNameException {
        Optional<Person> checkEmailExists = personRepository.findByEmail(person.getEmail());
        if (checkEmailExists.isPresent()) {
            throw new IncorretNameException("This email is already taken!");
        }
    }

    //method to check if mobile number exists
    private void checkMobileNumber(Person person) throws MobileNumberException {
        Optional<Object> savePerson = personRepository.getPersonByMobileNumber(person.getMobileNumber());
        if (savePerson.isPresent()) {
            throw new MobileNumberException(String.format("This mobile number %s is already used", person.getMobileNumber()));
        }
    }

    private void checkToHaveSpecificCharacterInEmail(Person person) throws IncorretNameException {
        Optional<Person> checkEmail = personRepository.findByEmail(person.getEmail());
        if (checkEmail.isPresent()) {
            if (!person.getEmail().contains("@yahoo") || !person.getEmail().contains("@gmail")) {
                throw new IncorretNameException("Invalid email, only accepted @yahoo and @gmail");
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
        checkToHaveSpecificCharacterInEmail(person);
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

    public List<PersonDTO> searchByFirstNameAndOrLastName(@Param("firstName") String firstName,
                                                          @Param("lastName") String lastName) {
        List<Person> personList = new ArrayList<>();
        List<PersonDTO> personDTOList = new ArrayList<>();
        personList.addAll(personRepository.searchFirstNameOrLastName(firstName, lastName).isEmpty() ? null : personRepository.searchFirstNameOrLastName(firstName, lastName));
        for (Person person : personList) {
            PersonDTO personDTO = mapper.map(person, PersonDTO.class);
            personDTOList.add(personDTO);
        }
        return personDTOList;

    }

    public void generateExcel(HttpServletResponse httpServletResponse) throws IOException {
        List<Person> personList = personRepository.findAll();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("List With Persons");
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("ID");
        row.createCell(0).setCellValue("First Name");
        row.createCell(0).setCellValue("Last Name");
        row.createCell(0).setCellValue("Email");

        int dataRowIndex = 1;
        for (Person person : personList) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(person.getPersonId());
            dataRow.createCell(1).setCellValue(person.getFirstName());
            dataRow.createCell(2).setCellValue(person.getLastName());
            dataRow.createCell(3).setCellValue(person.getEmail());
            dataRowIndex++;
        }
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
    }
}
