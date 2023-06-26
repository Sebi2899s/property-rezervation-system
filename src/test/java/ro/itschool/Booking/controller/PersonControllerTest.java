package ro.itschool.Booking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
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
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.service.PersonService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
class PersonControllerTest {
    private MockMvc mvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PersonService personService;


    @BeforeEach
    public void setup() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @WithMockUser("/bk/person")
    @Test
    public void testGetPersonById() throws Exception {
        Person person = new Person();
        long personId = 13213L;
        person.setPersonId(personId);
        person.setLastName("sss");
        person.setFirstName("sss32");
        person.setEmail("sss@dfa.com");
        personService.savePerson(person);
        String jsonRequest = mapper.writeValueAsString(person);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/get-by-id/{id}",personId).content(jsonRequest).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        assertEquals(200, result.getResponse().getStatus());


    }


}