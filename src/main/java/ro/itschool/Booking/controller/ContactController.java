package ro.itschool.Booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.itschool.Booking.customException.IncorrectDateException;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.entity.Contact;
import ro.itschool.Booking.service.ContractService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ContactController {

    @Autowired
    private ContractService contractService;

    @RequestMapping(value = "/contacts")
    public List<Contact> getAllContacts() {
        return contractService.getAllContacts() == null ? new ArrayList<>() : contractService.getAllContacts();
    }

    @RequestMapping(value = "/contact/{id}")
    public Contact getContactById(@PathVariable Long id) throws IncorrectIdException {
        return contractService.getContactById(id);
    }

    @RequestMapping(value = "/save")
    public Contact saveContact(@RequestBody Contact contact) throws IncorrectDateException {
        return contractService.createContact(contact);
    }

    @RequestMapping(value = "/update/{id}")
    public Contact saveContact(@RequestBody Contact contact, @PathVariable Long id) throws IncorrectIdException {
        Contact contactById = contractService.getContactById(id);

        return contractService.updateContact(id, contactById);
    }

    @RequestMapping(value = "/delete/{id}")
    public Long deleteContact(@PathVariable Long id) throws IncorrectIdException {
        return contractService.deleteContact(id);
    }
}
