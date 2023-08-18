package ro.itschool.Booking.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.itschool.Booking.Dto.ReservationRequestDTO;
import ro.itschool.Booking.customException.FieldValueException;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.customException.PersonNotFoundException;
import ro.itschool.Booking.entity.Reservation;
import ro.itschool.Booking.service.ReservationService;

import java.io.IOException;
import java.time.LocalDate;
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

    @PutMapping("/reservation/update/{reservationId}")
    public Reservation updateReservation(@RequestBody ReservationRequestDTO reservationRequestDTO, @PathVariable Long reservationId) throws IncorrectIdException, PersonNotFoundException, FieldValueException {

        return reservationService.updateOrSaveReservation(reservationRequestDTO,reservationId);
    }

    @PostMapping("/reservation")
    public Reservation saveReservation(@RequestBody ReservationRequestDTO reservationRequestDTO) throws IncorrectIdException, FieldValueException, PersonNotFoundException {


        return reservationService.updateOrSaveReservation(reservationRequestDTO, null);
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
