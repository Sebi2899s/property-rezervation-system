package ro.itschool.Booking.service;

import jakarta.annotation.Nullable;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.entity.Property;
import ro.itschool.Booking.entity.Reservation;
import ro.itschool.Booking.repository.ReservationRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private PersonService personService;
    @Autowired
    private PropertyService propertyService;

    private final double TAX_ADDED = 1.2;

    public void calculatePriceWithTax(@Nullable Long reservationId, String checkIn, String checkOut, Reservation reservation) throws IncorrectIdException {


        if (reservationId == null) {
            Double tax;
            if (reservation.getPrice() == null) {
                throw new RuntimeException("This reservation have no price!");
            }

            Long daysForReservation = findHowManyDaysAreInReservation(checkIn, checkOut);
            Double totalPrice = daysForReservation * reservation.getPrice();
            tax = TAX_ADDED * totalPrice;
            reservation.setPrice(tax);
        } else {

            Reservation reservationById = getReservationById(reservationId).get();
            double tax = 0L;
            if (reservationById.getPrice() == null) {
                throw new RuntimeException("This price is null, you need to add value!");
            }
            Long daysForReservation = findHowManyDaysAreInReservation(checkIn, checkOut);
            Double totalPrice = daysForReservation * reservationById.getPrice();
            tax = TAX_ADDED * totalPrice;
            reservationById.setPrice(tax);
        }

    }

    public Reservation saveReservation(Long personId, Long propertyId, String checkIn, String checkOut, Double price) throws IncorrectIdException {
        Reservation reservation = new Reservation();
        Person person = personService.findById(personId).get();
        Property property = propertyService.findById(propertyId).get();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu/MM/dd");
        LocalDate checkInDate = LocalDate.parse(checkIn, formatter);
        LocalDate checkOutDate = LocalDate.parse(checkOut, formatter);
        reservation.setCheckOutDate(checkOutDate);
        reservation.setCheckInDate(checkInDate);
        reservation.setProperty(property);
        reservation.setPerson(person);
        reservation.setPrice(price);

        calculatePriceWithTax(null, checkIn, checkOut, reservation);

        return reservationRepository.save(reservation);
    }

    private static Long findHowManyDaysAreInReservation(String checkInDate, String checkOutDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu/MM/dd");
        LocalDate checkInInfo = LocalDate.parse(checkInDate, formatter);
        LocalDate checkOutInfo = LocalDate.parse(checkOutDate, formatter);
        Long calculationByDay = ChronoUnit.DAYS.between(checkInInfo, checkOutInfo);
        return calculationByDay;
    }

    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Optional<Reservation> getReservationById(Long id) throws IncorrectIdException {
        Optional<Reservation> getReservation = reservationRepository.findById(id);
        getReservation.orElseThrow(() -> new IncorrectIdException("This is id:" + id + "was not found!"));
        return getReservation;
    }

    public String deleteReservation(Long id) throws IncorrectIdException {
        if (id.equals(null)) {
            throw new IncorrectIdException("This is id:" + id + " was not found!");
        }
        reservationRepository.deleteById(id);
        return "Reservation with id:" + id + " was deleted";
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public void generateExcel(HttpServletResponse httpServletResponse) throws IOException {
        List<Reservation> reservationList = reservationRepository.findAll();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("List With Reservations");
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("ID");
        row.createCell(1).setCellValue("Check-In");
        row.createCell(2).setCellValue("Check-Out");
        row.createCell(3).setCellValue("Price");
        row.createCell(4).setCellValue("Person");
        row.createCell(5).setCellValue("Property");

        int dataRowIndex = 1;
        for (Reservation reservation : reservationList) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);
            verificationForReservationFieldsAndGiveDefaultValue(reservation);
            try {
                dataRow.createCell(0).setCellValue(reservation.getId());
                dataRow.createCell(1).setCellValue(reservation.getCheckInDate());
                dataRow.createCell(2).setCellValue(reservation.getCheckOutDate());
                dataRow.createCell(3).setCellValue(reservation.getPrice());
                dataRow.createCell(4).setCellValue(reservation.getPerson().getFirstName() + " " + reservation.getPerson().getLastName());
                dataRow.createCell(5).setCellValue(reservation.getProperty().getPropertyName());
                if (reservation.getPerson() == null || reservation.getProperty() == null) {
                    throw new RuntimeException("The reservation with id" + reservation.getId() + "is not valid!");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            dataRowIndex++;
        }
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
    }

    private static void verificationForReservationFieldsAndGiveDefaultValue(Reservation reservation) {
        if (reservation.getCheckInDate() == null) {
            reservation.setCheckInDate(LocalDate.of(2020, 01, 01));
        }
        if (reservation.getCheckOutDate() == null) {
            reservation.setCheckOutDate(LocalDate.of(2020, 01, 02));
        }
        if (reservation.getPrice() == null) {
            reservation.setPrice(10.5);
        }

    }
}
