package ro.itschool.Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.itschool.Booking.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact,Long> {
}
