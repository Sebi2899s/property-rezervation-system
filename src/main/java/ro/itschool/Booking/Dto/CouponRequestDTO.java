package ro.itschool.Booking.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CouponRequestDTO {
    private String code;
    private String validFrom;
    private String validTo;
    private double discount;
    private String country;
    private boolean activCoupon;
}
