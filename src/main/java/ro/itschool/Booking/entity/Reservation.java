package ro.itschool.Booking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


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

    private Double price;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private String country;
}
