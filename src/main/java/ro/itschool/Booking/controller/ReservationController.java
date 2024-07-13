package ro.itschool.Booking.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.itschool.Booking.Dto.ReservationRequestDTO;
import ro.itschool.Booking.customException.*;
import ro.itschool.Booking.entity.Reservation;
import ro.itschool.Booking.entity.Status;
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

    @GetMapping(value = "/excel")
    public void generateExcelReport(HttpServletResponse response) throws IOException {


        response.setContentType("application");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=reservations.xlsx";

        response.setHeader(headerKey, headerValue);
        reservationService.generateExcel(response);
    }


    @PutMapping("/reservation/update/{reservationId}")
    public Status updateReservation(@RequestBody ReservationRequestDTO reservationRequestDTO, @PathVariable Long reservationId) throws IncorrectIdException, PersonNotFoundException, FieldValueException, IncorrectDateException, BlockedDaysException {
        Status status = new Status();
        reservationService.updateOrSaveReservation(reservationRequestDTO, reservationId);

        status.setMessage("Reservation updated successfully !");
        status.setId(reservationId);

        return status;
    }

    @PostMapping("/reservation")
    public Status saveReservation(@RequestBody ReservationRequestDTO reservationRequestDTO) throws IncorrectIdException, FieldValueException, PersonNotFoundException, IncorrectDateException, BlockedDaysException {
        Status status = new Status();

        Reservation reservation = reservationService.updateOrSaveReservation(reservationRequestDTO, null);

        status.setMessage("Reservation saved successfully !");
        status.setId(reservation.getId());
        return status;
    }

    @DeleteMapping("/reservation/delete/{id}")
    public Status deleteReservation(Long id) throws IncorrectIdException {

        Status status = new Status();
        reservationService.deleteReservation(id);

        status.setMessage("Reservation deleted successfully !");
        status.setId(id);

        return status;
    }


}
