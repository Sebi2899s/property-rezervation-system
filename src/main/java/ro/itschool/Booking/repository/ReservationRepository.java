package ro.itschool.Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.itschool.Booking.entity.Reservation;
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
