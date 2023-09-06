package ro.itschool.Booking.Dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import ro.itschool.Booking.entity.RoomReservation;

import java.util.List;

@Getter
@Setter
public class RoomRequest {
    private String description;

    private String type;

    private boolean bookingEnabled;
    private Long roomReservationId;
}
