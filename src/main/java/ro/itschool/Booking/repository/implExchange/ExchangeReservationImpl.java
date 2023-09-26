package ro.itschool.Booking.repository.implExchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.entity.Reservation;
import ro.itschool.Booking.entity.Room;
import ro.itschool.Booking.repository.ExchangeReservationRepository;
import ro.itschool.Booking.repository.ReservationRepository;
import ro.itschool.Booking.service.ReservationService;
import ro.itschool.Booking.service.RoomService;

import java.time.LocalDate;
import java.util.List;
@Service
public class ExchangeReservationImpl implements ExchangeReservationRepository {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomService roomService;


    @Override
    public boolean checkIfEligibleForExchange(Long reservationId, LocalDate newCheckIn, LocalDate newCheckOut) throws IncorrectIdException {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new NotFoundException("This reservation with id " + reservationId + " was not found!"));
        LocalDate currentCheckIn = reservation.getCheckInDate();
        LocalDate currentCheckOut = reservation.getCheckOutDate();
        if (newCheckIn.isEqual(currentCheckIn) && newCheckOut.isEqual(currentCheckOut)) {
            return false;
        }
        if (newCheckIn.isAfter(newCheckOut)) {
            return false;
        }
        List<Room> availableRooms = roomService.getAllRoomsByAvailability(newCheckIn, newCheckOut);
        if (availableRooms.isEmpty()) {
            return false;
        }

        return true;
    }

    @Override
    public List<Reservation> getEligibleReservationsForExchange(Long appointmentId) {
        return null;
    }

    @Override
    public boolean checkIfExchangeIsPossible(Long oldAppointmentId, Long newAppointmentId, Long userId) {
        return false;
    }

    @Override
    public boolean acceptExchange(Long exchangeId, Long userId) {
        return false;
    }

    @Override
    public boolean rejectExchange(Long exchangeId, Long userId) {
        return false;
    }

    @Override
    public boolean requestExchange(Long oldAppointmentId, Long newAppointmentId, Long userId) {
        return false;
    }
}
