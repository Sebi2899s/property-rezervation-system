package ro.itschool.Booking.repository;

import org.hibernate.annotations.Formula;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ro.itschool.Booking.entity.Reminder;
import ro.itschool.Booking.entity.Reservation;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long>, PagingAndSortingRepository<Reminder, Long> {

}
