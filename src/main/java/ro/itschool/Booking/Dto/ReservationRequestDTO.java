package ro.itschool.Booking.Dto;

import jakarta.annotation.Nullable;
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
    private Long propertyId;
    @Nullable
    private Long couponId;
    private String checkIn;
    private String checkOut;
    private String country;

    private Double price;
    private String description;


}
