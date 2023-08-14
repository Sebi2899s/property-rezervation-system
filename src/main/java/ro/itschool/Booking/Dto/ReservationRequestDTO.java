package ro.itschool.Booking.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDTO {
    private Long personId;
    private String checkIn;
    private String checkOut;
    private Long propertyId;
    private Long couponId;
    private String country;

    private Double price;
    private String description;


}
