package ro.itschool.Booking.service;

import jakarta.annotation.Nullable;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ro.itschool.Booking.Dto.ReservationRequestDTO;
import ro.itschool.Booking.customException.FieldValueException;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.customException.PersonNotFoundException;
import ro.itschool.Booking.entity.Coupon;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.entity.Property;
import ro.itschool.Booking.entity.Reservation;
import ro.itschool.Booking.repository.ReservationRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private PersonService personService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private PropertyService propertyService;

    private final double TAX_ADDED = 2.5;

    //---------------------------------------------------------------------------------------------------------------------
    public Reservation saveReservation(Long personId, Long propertyId, String checkIn, String checkOut, Double price, @Nullable Long couponId, String country) throws IncorrectIdException {
        Reservation reservation = new Reservation();
        Person person = personService.findById(personId).get();
        Property property = propertyService.findById(propertyId).get();
        Coupon coupon = couponService.getCoupon(couponId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu/MM/dd");
        LocalDate checkInDate = LocalDate.parse(checkIn, formatter);
        LocalDate checkOutDate = LocalDate.parse(checkOut, formatter);
        reservation.setCheckOutDate(checkOutDate);
        reservation.setCheckInDate(checkInDate);
        reservation.setProperty(property);
        reservation.setPerson(person);
        reservation.setPrice(price);
        reservation.setCoupon(coupon);
        reservation.setCountry(country);

        calculatePriceWithTax(null, checkIn, checkOut, reservation, coupon);

        return reservationRepository.save(reservation);
    }


    //---------------------------------------------------------------------------------------------------------------------
    public void calculatePriceWithTax(@Nullable Long reservationId, String checkIn, String checkOut, Reservation reservation, Coupon coupon) throws IncorrectIdException {

        //first situation is when i don't have a reservation
        if (reservationId == null) {
            if (reservation.getPrice() == null) {
                throw new RuntimeException("This reservation have no price!");
            }
            Long daysForReservation = findHowManyDaysAreInReservation(checkIn, checkOut);
            Double totalPriceWithoutTax = daysForReservation * reservation.getPrice();

            //if cupon is active will be calculated in it, if isn't the price will be calculated without discount
            verificationIfCouponIsActiveAndCalculatePrice(reservation, coupon, totalPriceWithoutTax);

        } else {

            //this situation is when i have already a resevation and i wanted to update the price
            Reservation reservationById = getReservationById(reservationId).get();
            if (reservationById.getPrice() == null) {
                throw new RuntimeException("This reservation have no price!");
            }
            Long daysForReservation = findHowManyDaysAreInReservation(checkIn, checkOut);
            Double totalPriceWithoutTax = daysForReservation * reservationById.getPrice();

            //if coupon is active will be calculated in it, if isn't the price will be calculated without discount
            verificationIfCouponIsActiveAndCalculatePrice(reservation, coupon, totalPriceWithoutTax);
        }

    }


    //---------------------------------------------------------------------------------------------------------------------
    public Reservation updateOrSaveReservation(@NotNull ReservationRequestDTO reservationRequestDTO, @Nullable Long reservationId) throws IncorrectIdException, FieldValueException, PersonNotFoundException {

        if (reservationId == null) {
            Person personRq = personService.getPersonOrThrow(reservationRequestDTO.getPersonId());

            Property propertyRq = propertyService.getPropertyOrThrow(reservationRequestDTO.getPropertyId());

            Coupon couponRq = couponService.getCouponOrAnEmpty(reservationRequestDTO.getCouponId());


            String checkInRq = reservationRequestDTO.getCheckIn();

            String checkOutRq = reservationRequestDTO.getCheckOut();

            Double priceRq = reservationRequestDTO.getPrice();

            String countryRq = reservationRequestDTO.getCountry();

            String descriptionRq = reservationRequestDTO.getDescription();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

            LocalDate checkInDateRq = LocalDate.parse(checkInRq, formatter);

            LocalDate checkOutDateRq = LocalDate.parse(checkOutRq, formatter);


            Reservation reservation = Reservation.builder()
                    .person(personRq)
                    .property(propertyRq)
                    .checkInDate(checkInDateRq)
                    .checkOutDate(checkOutDateRq)
                    .coupon(couponRq)
                    .country(countryRq)
                    .price(priceRq)
                    .description(descriptionRq)
                    .build();

            calculatePriceWithTax(reservationId, checkInRq, checkOutRq, reservation, couponRq);

            return save(reservation);
        } else {
            Long personId;
            if (reservationRequestDTO.getPersonId() != null) {
                personId = reservationRequestDTO.getPersonId();
            } else {
                throw new FieldValueException("This field must have a value in this field!");
            }

            Long propertyId;
            if (reservationRequestDTO.getPropertyId() != null) {

                propertyId = reservationRequestDTO.getPropertyId();
            } else {
                throw new FieldValueException("This field must have a value in this field!");
            }
            String checkIn;
            if (reservationRequestDTO.getCheckIn() != null) {
                checkIn = reservationRequestDTO.getCheckIn();
            } else {
                throw new FieldValueException("This field must have a value in this field!");
            }

            String checkOut;
            if (reservationRequestDTO.getCheckOut() != null) {
                checkOut = reservationRequestDTO.getCheckOut();
            } else {
                throw new FieldValueException("This field must have a value in this field!");
            }

            Double price = reservationRequestDTO.getPrice();
            Long couponId = reservationRequestDTO.getCouponId();
            String country = reservationRequestDTO.getCountry();

            return saveReservation(personId, propertyId, checkIn, checkOut, price, couponId, country);
        }
    }


    //---------------------------------------------------------------------------------------------------------------------

    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }


    //---------------------------------------------------------------------------------------------------------------------

    public Optional<Reservation> getReservationById(Long id) throws IncorrectIdException {
        Optional<Reservation> getReservation = reservationRepository.findById(id);
        getReservation.orElseThrow(() -> new IncorrectIdException("This is id:" + id + "was not found!"));
        return getReservation;
    }

    //---------------------------------------------------------------------------------------------------------------------

    public String deleteReservation(Long id) throws IncorrectIdException {
        if (id == null) {
            throw new IncorrectIdException("This is id:" + id + " was not found!");
        }
        reservationRepository.deleteById(id);
        return "Reservation with id:" + id + " was deleted";
    }


    //---------------------------------------------------------------------------------------------------------------------

    public List<Reservation> getAllReservations() {

        List<Reservation> allReservation = reservationRepository.findAll();
        if (allReservation == null) {
            return new ArrayList<>();
        } else {
            return allReservation;
        }
    }

    //---------------------------------------------------------------------------------------------------------------------
    public List<Reservation> getAllReservationForAdmin(Integer pageNo,
                                                       Integer pageSize,
                                                       String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Reservation> pagedResult = reservationRepository.findAll(paging);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Reservation>();
        }
    }

    //---------------------------------------------------------------------------------------------------------------------

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
        row.createCell(6).setCellValue("Coupon");

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
                dataRow.createCell(6).setCellValue(reservation.getCoupon().getCode());
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

    //---------------------------------------------------------------------------------------------------------------------

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

    //---------------------------------------------------------------------------------------------------------------------
    private static Long findHowManyDaysAreInReservation(String checkInDate, String checkOutDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu/MM/dd");
        LocalDate checkInInfo = LocalDate.parse(checkInDate, formatter);
        LocalDate checkOutInfo = LocalDate.parse(checkOutDate, formatter);
        Long calculationByDay = ChronoUnit.DAYS.between(checkInInfo, checkOutInfo);
        return calculationByDay;
    }


    //---------------------------------------------------------------------------------------------------------------------

    private static Double getValuePercentageOfDiscount(Coupon coupon) {
        Double couponDiscount = coupon.getDiscount();
        double finalValue = couponDiscount / 100;
        return finalValue;
    }

    //---------------------------------------------------------------------------------------------------------------------
    private void verificationIfCouponIsActiveAndCalculatePrice(Reservation reservation, Coupon coupon, Double totalPrice) {
        double finalPrice;
        if (coupon.isActivCoupon()) {
            //getValuePercentageOfDiscount this method return the discount value
            Double couponDiscount = getValuePercentageOfDiscount(coupon);

            //calculate the price with tax
            finalPrice = totalPrice - ((TAX_ADDED / 100) * totalPrice);

            //calculate the price with discount if exists
            finalPrice = finalPrice - (finalPrice * couponDiscount);
            reservation.setPrice(finalPrice);
        } else {
            finalPrice = totalPrice - ((TAX_ADDED / 100) * totalPrice);
            reservation.setPrice(finalPrice);
        }
    }
    //---------------------------------------------------------------------------------------------------------------------

}
