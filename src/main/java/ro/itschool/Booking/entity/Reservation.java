package ro.itschool.Booking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @ToString.Exclude
    private Person person;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @ToString.Exclude
    private Property property;
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    private Coupon coupon;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reservation")
    @JsonIgnore
    private List<Reminder> reminders;

    private Double price;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private String country;

    private String description;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
}
