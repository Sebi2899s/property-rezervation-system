package ro.itschool.Booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String type;

    private boolean bookingEnabled;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "room")
    private List<RoomReservation> roomReservations;

    @OneToMany(mappedBy = "room")
    private List<Reservation> reservations;
}
