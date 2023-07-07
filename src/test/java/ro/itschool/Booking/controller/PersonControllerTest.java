package ro.itschool.Booking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ro.itschool.Booking.BookingApplication;
import ro.itschool.Booking.DtoEntity.PersonDTO;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.entity.Role;
import ro.itschool.Booking.repository.PersonRepository;
import ro.itschool.Booking.service.PersonService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PersonControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    WebApplicationContext context;

    @Mock
    private PersonRepository personRepository;

    @BeforeEach
    public void setup() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
//        personService = new PersonService(repository);
    }

    @Test
    public void testGetPersonById() throws Exception {
        Person person = new Person();
        long personId = 13213L;
        person.setPersonId(personId);
        person.setLastName("sss");
        person.setFirstName("sss32");
        person.setEmail("sss@dfa.com");
        person.setRole(Role.ROLE_USER);
        person.setMobileNumber("113");


        Mockito.when(personRepository.findById(personId)).thenReturn(Optional.of(person));

        Optional<Person> result = personRepository.findById(personId);


        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("get-by-id/{personId}").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(person.getEmail(), result.get().getEmail());
        Assertions.assertEquals(person.getMobileNumber(), result.get().getMobileNumber());
//        personService.savePerson(person);
//        String jsonRequest = mapper.writeValueAsString(person);
//        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/get-by-id/{personId}",personId)
//                .content(jsonRequest)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk()).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());


    }

    @Test
    public void getTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/get-by-id/{id}",1L).accept(MediaType.APPLICATION_JSON)).andDo(System.out::println).andExpect(status().isOk());
    }
}