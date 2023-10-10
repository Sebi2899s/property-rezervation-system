package ro.itschool.Booking.repository;

import org.springframework.stereotype.Repository;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.entity.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ExchangeReservationRepository {
    boolean checkIfEligibleForExchange(Long reservationId, LocalDate newCheckIn, LocalDate newCheckOut) throws IncorrectIdException;

    List<Reservation> getEligibleReservationsForExchange(Long appointmentId);

}
