package ro.itschool.Booking.service.email.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ro.itschool.Booking.customException.InvalidMailException;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.service.email.EmailBodyService;

import java.util.List;

@Service
public class EmailSenderServiceImpl implements EmailBodyService {
    private final JavaMailSender sender;

    @Autowired
    public EmailSenderServiceImpl(JavaMailSender sender) {
        this.sender = sender;
    }


    public void sendEmail(@NonNull String email, String content, String subject) {


        try {


            String emailCheckForNull = email == null ? "none" : email;

            if (!emailCheckForNull.contains("@") && (emailCheckForNull.equals("none"))) {

                throw new InvalidMailException("This email is not valid");
            }
            String checkContentForNull = content == null ? "  " : content;
            String checkSubjectForNull = subject == null ? "No subject added" : subject;

            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom("conttesttest4@gmail.com");
            message.setSubject(checkSubjectForNull);
            message.setText(checkContentForNull);
            message.setTo(emailCheckForNull);

            sender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


