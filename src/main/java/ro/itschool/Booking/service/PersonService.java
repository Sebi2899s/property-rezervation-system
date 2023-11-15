package ro.itschool.Booking.service;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ro.itschool.Booking.Dto.PersonDTO;
import ro.itschool.Booking.customException.PersonNotFoundException;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.customException.IncorretNameException;
import ro.itschool.Booking.customException.MobileNumberException;
import ro.itschool.Booking.entity.Role;
import ro.itschool.Booking.repository.PersonRepository;
import ro.itschool.Booking.repository.PropertyRepository;
import ro.itschool.Booking.specifications.PersonRoleAndFirstNameRequest;
import ro.itschool.Booking.specifications.QuerySpecificationsDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private QuerySpecificationsDao querySpecificationsDao;


    public PersonService(PersonRepository repository) {
    }


    //GET
//---------------------------------------------------------------------------------------------------------------------
    public List<Person> getAllPersons(Integer pageNo,
                                      Integer pageSize,
                                      String sortBy) {

        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Person> pagedResult = personRepository.findAll(paging);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Person>();
        }
    }

//\---------------------------------------------------------------------------------------------------------------------

    public Person getByIdPerson(Long id) {
        if (id == null) {
            throw new RuntimeException("This id must not be null");
        }
        return personRepository.findById(id).orElse(new Person());
    }


    //\---------------------------------------------------------------------------------------------------------------------
    public Optional<PersonDTO> getByIdPersonDTO(Long id) throws IncorrectIdException {
        Optional<Person> person = personRepository.findById(id);
        PersonDTO personDto = mapper.map(person, PersonDTO.class);
        if (person.isEmpty()) {
            throw new IncorrectIdException("This id " + id + " was not found!");
        } else {
            return Optional.ofNullable(personDto);
        }
    }




    

//---------------------------------------------------------------------------------------------------------------------


    public Person getPersonOrThrow(Long id) throws PersonNotFoundException {
        Optional<Person> person = personRepository.findById(id);
        return person.orElseThrow(() -> new PersonNotFoundException("this person is not found"));
    }


    //Post
//---------------------------------------------------------------------------------------------------------------------
    public Person savePerson(Person person) throws MobileNumberException, IncorretNameException {
        checkEmailExists(person);
        checkToHaveSpecificCharacterInEmail(person);
        checkMobileNumber(person);
        person.setRole(Role.ROLE_USER);
        return personRepository.save(person);

    }


    //---------------------------------------------------------------------------------------------------------------------
    private void checkEmailExists(Person person) throws IncorretNameException {
        Optional<Person> checkEmailExists = personRepository.findByEmail(person.getEmail());
        if (checkEmailExists.isPresent()) {
            throw new IncorretNameException("This email is already taken!");
        }
    }


    //---------------------------------------------------------------------------------------------------------------------
    //method to check if mobile number exists
    private void checkMobileNumber(Person person) throws MobileNumberException {
        Optional<Object> savePerson = personRepository.getPersonByMobileNumber(person.getMobileNumber());
        if (savePerson.isPresent()) {
            throw new MobileNumberException(String.format("This mobile number %s is already used", person.getMobileNumber()));
        }
    }


    //---------------------------------------------------------------------------------------------------------------------
    private void checkToHaveSpecificCharacterInEmail(Person person) throws IncorretNameException {
        Optional<Person> checkEmail = personRepository.findByEmail(person.getEmail());
        if (checkEmail.isPresent()) {
            if (!person.getEmail().contains("@yahoo") || !person.getEmail().contains("@gmail")) {
                throw new IncorretNameException("Invalid email, only accepted @yahoo and @gmail");
            }
        }
    }

    //UPDATE
//---------------------------------------------------------------------------------------------------------------------
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
//---------------------------------------------------------------------------------------------------------------------

    public void deletePerson(Long id) throws IncorrectIdException {
        if (id == null) {
            throw new IncorrectIdException("This id " + id
                    + " is not found");
        } else {
            Person person_p = (personRepository.findById(id).orElseThrow(() -> new IncorrectIdException("There are no person that have this id:" + id)));
            personRepository.delete(person_p);
        }
    }

//---------------------------------------------------------------------------------------------------------------------

    public Optional<Person> findByEmail(String email) {
        return Optional.ofNullable(personRepository.findByEmail(email).orElse(new Person()));
    }


//---------------------------------------------------------------------------------------------------------------------

    public Optional<Person> findById(Long id) {
        return personRepository.findById(id);

    }


    // generate EXCEL
    //---------------------------------------------------------------------------------------------------------------------
    public void generateExcel(HttpServletResponse httpServletResponse) throws IOException {
        List<Person> personList = personRepository.findAll();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("List With Persons");
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("ID");
        row.createCell(1).setCellValue("First Name");
        row.createCell(2).setCellValue("Last Name");
        row.createCell(3).setCellValue("Email");

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

    //---------------------------------------------------------------------------------------------------------------------
    public Person updateOrSavePerson(Person personRequest, Long personId) throws MobileNumberException, IncorretNameException {
        if (personId == null) {
            return savePerson(personRequest);
        } else {
            Person person = findById(personId).get();
            if (person != null) {
                person.setEmail(personRequest.getEmail());
                person.setPersonId(personRequest.getPersonId());
                person.setLastName(personRequest.getLastName());
                person.setFirstName(personRequest.getFirstName());
                person.setProperty(personRequest.getProperty());
                person.setPassword(personRequest.getPassword());
                person.setMobileNumber(personRequest.getMobileNumber());
                person.setReservations(personRequest.getReservations());
                person.setSubscriber(personRequest.isSubscriber());

            }
            return savePerson(person);
        }
    }

    public List<Person> getPropertyByPersonFirstName(String propertyName) {

        return personRepository.getAllPersonsByPropertiesName(propertyName);
    }

    public List<Person> getPropertyByNameFilter(String name) {
        List<Person> personList = personRepository.getPersonsByFirstName(name);
        if (personList.isEmpty() || personList == null) {
            return new ArrayList<>();
        }
        return personList;
    }

    public List<Person> getPersonNameAndRole(PersonRoleAndFirstNameRequest personRoleAndFirstNameRequest) {
        return querySpecificationsDao.getAllPersonByFirstNameOrMobileNumber(personRoleAndFirstNameRequest);
    }
}
