package ro.itschool.Booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.itschool.Booking.service.email.impl.EmailSenderServiceImpl;

import java.util.Map;

@RestController
@RequestMapping(value = "/email")
public class EmailController {
    @Autowired
    private EmailSenderServiceImpl emailService;

    @PostMapping(value = "/send")
    public ResponseEntity<String> emailSender(@RequestBody Map<String, String> request) {
        emailService.sendEmail(request.get("email"), request.get("content"), request.get("subject"));

        return new ResponseEntity<>("Email sent successfully", HttpStatus.OK);
    }
}
