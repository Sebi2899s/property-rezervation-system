package ro.itschool.Booking.service;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.entity.Property;
import ro.itschool.Booking.entity.Reservation;
import ro.itschool.Booking.repository.ReservationRepository;

import java.sql.Date;
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

    public Reservation saveOrUpdateReservation(@NotNull Person person, @Nullable Long id, @Nullable String checkIn, @Nullable String checkOut) throws IncorrectIdException {
        Property property = new Property();
        property=person.getProperty();
        Reservation reservation =new Reservation();
        if (id == null) {
            reservation = Reservation.builder()
                    .price(property.getPrice())
                    .person(person)
                    .property(property)
                    .checkInDate(Date.valueOf(checkIn))
                    .checkOutDate(Date.valueOf(checkOut))
                    .build();

        } else {
            reservation = getReservationById(id).get();
            reservation.setPerson(person);
            reservation.setPrice(person.getProperty().getPrice());
            reservation.setProperty(person.getProperty());
            reservation.setCheckInDate(Date.valueOf(checkIn));
            reservation.setCheckOutDate(Date.valueOf(checkOut));
            reservation = reservationRepository.save(reservation);

        }
        return reservationRepository.save(reservation);
    }


    public Reservation saveReservation(Long personId,Long propertyId, String checkIn, String checkOut){
        Reservation reservation = new Reservation();
        Person person = personService.findById(personId).get();
        Property property = propertyService.findById(propertyId).get();
        Date checkInDate =Date.valueOf(checkIn);
        Date checkOutDate =Date.valueOf(checkOut);
        reservation.setCheckOutDate(checkOutDate);
        reservation.setCheckInDate(checkInDate);
        reservation.setProperty(property);
        reservation.setPerson(person);
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
}
