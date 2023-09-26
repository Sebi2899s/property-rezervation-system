package ro.itschool.Booking.repository;

import org.springframework.stereotype.Repository;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.entity.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ExchangeReservationRepository {
    boolean checkIfEligibleForExchange(Long reservationId, LocalDate newCheckIn, LocalDate newCheckOut) throws IncorrectIdException;

    List<Reservation> getEligibleReservationsForExchange(Long appointmentId);

    boolean checkIfExchangeIsPossible(Long oldAppointmentId, Long newAppointmentId, Long userId);

    boolean acceptExchange(Long exchangeId, Long userId);

    boolean rejectExchange(Long exchangeId, Long userId);

    boolean requestExchange(Long oldAppointmentId, Long newAppointmentId, Long userId);
}
