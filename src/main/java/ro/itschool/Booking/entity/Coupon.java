package ro.itschool.Booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private LocalDate validFrom;
    private LocalDate validTo;
    private double discount;

    private boolean activCoupon;
    @OneToMany(mappedBy = "coupon")
    private List<Reservation> reservations;


}
