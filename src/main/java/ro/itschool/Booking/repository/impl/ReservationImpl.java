package ro.itschool.Booking.repository.impl;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import ro.itschool.Booking.Dto.ReservationRequestDTO;
import ro.itschool.Booking.customException.FieldValueException;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.entity.Coupon;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.entity.Property;
import ro.itschool.Booking.entity.Reservation;
import ro.itschool.Booking.repository.ReservationRepositoryService;
import ro.itschool.Booking.service.CouponService;
import ro.itschool.Booking.service.PersonService;
import ro.itschool.Booking.service.PropertyService;
import ro.itschool.Booking.service.ReservationService;

import java.time.LocalDate;

public class ReservationImpl implements ReservationRepositoryService {

    @Autowired
    private PersonService personService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private ReservationService reservationService;

    @SneakyThrows
    @Override
    public Reservation updateOrSaveReservation(ReservationRequestDTO reservationRequestDTO, Long reservationId) {
        if (reservationId != null) {
            Person person = personService.getPersonOrThrow(reservationRequestDTO.getPersonId());
            Property property = propertyService.getPropertyOrThrow(reservationRequestDTO.getPropertyId());
            Coupon coupon = couponService.getCoupon(reservationRequestDTO.getCouponId());
            String checkIn = reservationRequestDTO.getCheckIn();
            String checkOut = reservationRequestDTO.getCheckOut();
            LocalDate checkInDate = LocalDate.parse(checkIn);
            LocalDate checkOutDate = LocalDate.parse(checkOut);
            Reservation reservation = new Reservation();
            reservation.setPerson(person);
            reservation.setProperty(property);
            reservation.setCheckInDate(checkInDate);
            reservation.setCheckOutDate(checkOutDate);
            reservation.setCoupon(coupon);
            reservationService.calculatePriceWithTax(reservationId, checkIn, checkOut, reservation, coupon);
            return reservationService.save(reservation);
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

            return reservationService.saveReservation(personId, propertyId, checkIn, checkOut, price, couponId, country);
        }
    }
}
