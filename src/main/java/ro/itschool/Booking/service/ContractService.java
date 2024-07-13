package ro.itschool.Booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.itschool.Booking.customException.IncorrectDateException;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.entity.Contact;
import ro.itschool.Booking.repository.ContactRepository;
import ro.itschool.Booking.service.email.impl.EmailSenderServiceImpl;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContractService {

    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private EmailSenderServiceImpl emailSenderService;

    public List<Contact> getAllContacts() {
        List<Contact> allContacts = contactRepository.findAll();
        return allContacts == null ? new ArrayList<>() : allContacts;
    }

    public Contact getContactById(Long id) throws IncorrectIdException {
        return contactRepository.findById(id).orElseThrow(() -> new IncorrectIdException("No contact exists with this id " + id));
    }

    @Transactional
    public Contact createContact(Contact contact) throws IncorrectDateException {
        sendEmailWithInformationFromContact(contact);
        if (contact == null) {
            throw new IncorrectDateException("This contact should not be null");
        } else {
            return contactRepository.save(contact);
        }
    }

    private void sendEmailWithInformationFromContact(Contact contact) {
        if (contact != null) {
            emailSenderService.sendEmail(contact.getEmail(), contact.getBody(), contact.getSubject());
        }
    }


    public Contact updateContact(Long id, Contact contactDetails) throws IncorrectIdException {
        Contact contact = getContactById(id);

        contact.setFirstName(contactDetails.getFirstName());
        contact.setLastName(contactDetails.getLastName());
        contact.setEmail(contactDetails.getEmail());
        contact.setPhoneNumber(contactDetails.getPhoneNumber());
        contact.setBody(contactDetails.getBody());
        contact.setSubject(contactDetails.getSubject());
        return contactRepository.save(contact);
    }

    public Long deleteContact(Long id) throws IncorrectIdException {
        Contact contact = getContactById(id);
        contactRepository.delete(contact);
        return id;
    }
}

