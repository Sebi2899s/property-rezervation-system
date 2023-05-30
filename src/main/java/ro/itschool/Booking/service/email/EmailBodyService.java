package ro.itschool.Booking.service.email;

import org.springframework.stereotype.Repository;
@Repository
public interface EmailBodyService {

    public void sendEmail(String email, String content, String subject) ;
}
