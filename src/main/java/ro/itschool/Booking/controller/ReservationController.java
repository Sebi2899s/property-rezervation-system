package ro.itschool.Booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.itschool.Booking.DtoEntity.ReservationRequestDTO;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.entity.Reservation;
import ro.itschool.Booking.service.ReservationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/reservation/{id}")
    public Optional<Reservation> getReservationById(@RequestParam Long id) throws IncorrectIdException {
        return reservationService.getReservationById(id);
    }

    @GetMapping("/reservations")
    public List<Reservation> reservationsList() {
        List<Reservation> allReservations = new ArrayList<>();
        allReservations.addAll(reservationService.getAllReservations());

        return allReservations;
    }

    @PutMapping("/reservation/update")
    public Reservation updateReservation(@RequestBody ReservationRequestDTO reservationRequestDTO) throws IncorrectIdException {
//        Person person = reservationRequestDTO.getPerson();
        String checkIn = reservationRequestDTO.getCheckIn();
        String checkOut = reservationRequestDTO.getCheckOut();
//        Long id = reservationRequestDTO.getId();
//        return reservationService.saveOrUpdateReservation(person, id, checkIn, checkOut);
        return null;
    }

    @PostMapping("/reservation")
    public Reservation saveReservation(@RequestBody ReservationRequestDTO reservationRequestDTO) throws IncorrectIdException {
        Long personId = reservationRequestDTO.getPersonId();
        Long propertyId = reservationRequestDTO.getPropertyId();
        String checkIn = reservationRequestDTO.getCheckIn();
        String checkOut = reservationRequestDTO.getCheckOut();
        return reservationService.saveReservation(personId, propertyId, checkIn, checkOut);
    }

    @DeleteMapping("/reservation/delete/{id}")
    public String deleteReservation(Long id) throws IncorrectIdException {
        return reservationService.deleteReservation(id);
    }
}
