package ro.itschool.Booking.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.itschool.Booking.DtoEntity.ReservationRequestDTO;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.customException.PersonNotFoundException;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.entity.Property;
import ro.itschool.Booking.entity.Reservation;
import ro.itschool.Booking.service.PersonService;
import ro.itschool.Booking.service.PropertyService;
import ro.itschool.Booking.service.ReservationService;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private PersonService personService;
    @Autowired
    private PropertyService propertyService;
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

    @PutMapping("/reservation/update/{reservationId}")
    public Reservation updateReservation(@RequestBody ReservationRequestDTO reservationRequestDTO,@PathVariable Long reservationId) throws IncorrectIdException, PersonNotFoundException {
        Person person = personService.getPersonOrThrow(reservationRequestDTO.getPersonId());
        Property property = propertyService.getPropertyOrThrow(reservationRequestDTO.getPropertyId());
        String checkIn = reservationRequestDTO.getCheckIn();
        String checkOut = reservationRequestDTO.getCheckOut();
        LocalDate checkInDate= LocalDate.parse(checkIn);
        LocalDate checkOutDate= LocalDate.parse(checkOut);
        Reservation reservation = new Reservation();
        reservation.setPerson(person);
        reservation.setProperty(property);
        reservation.setCheckInDate(checkInDate);
        reservation.setCheckOutDate(checkOutDate);
        reservationService.calculatePriceWithTax(reservationId,checkIn,checkOut,reservation);
        return reservationService.save(reservation);
    }

    @PostMapping("/reservation")
    public Reservation saveReservation(@RequestBody ReservationRequestDTO reservationRequestDTO) throws IncorrectIdException {
        Long personId = reservationRequestDTO.getPersonId();
        Long propertyId = reservationRequestDTO.getPropertyId();
        String checkIn = reservationRequestDTO.getCheckIn();
        String checkOut = reservationRequestDTO.getCheckOut();
        Double price = reservationRequestDTO.getPrice();

        return reservationService.saveReservation(personId, propertyId, checkIn, checkOut,price);
    }

    @DeleteMapping("/reservation/delete/{id}")
    public String deleteReservation(Long id) throws IncorrectIdException {
        return reservationService.deleteReservation(id);
    }

    @GetMapping(value = "/excel")
    public void generateExcelReport(HttpServletResponse response) throws IOException {


        response.setContentType("application");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=reservations.xlsx";

        response.setHeader(headerKey, headerValue);
        reservationService.generateExcel(response);
    }

}
