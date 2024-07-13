package ro.itschool.Booking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;


@Entity
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    private Person person;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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

    private boolean breakfastRequested;
    @Transient
    private boolean canReserve;


    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public Reservation(Long id, Person person, Property property, Coupon coupon, List<Reminder> reminders, Double price, LocalDate checkInDate, LocalDate checkOutDate, String country, String description, boolean breakfastRequested, boolean canReserve, Room room) {
        this.id = id;
        this.person = person;
        this.property = property;
        this.coupon = coupon;
        this.reminders = reminders;
        this.price = price;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.country = country;
        this.description = description;
        this.breakfastRequested = breakfastRequested;
        this.canReserve = canReserve;
        this.room = room;
    }

    public Reservation(Person person, Property property, LocalDate checkIn, LocalDate checkOut, Double price, @Nullable Coupon coupon, String country, boolean breakfastRq) {
        this.person = person;
        this.property = property;
        this.checkInDate = checkOut;
        this.checkOutDate = checkOut;
        this.price = price;
        this.coupon = coupon;
        this.country = country;
        this.breakfastRequested = breakfastRq;
    }
}
