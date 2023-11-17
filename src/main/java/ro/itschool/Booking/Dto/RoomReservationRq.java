package ro.itschool.Booking.Dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomReservationRq {

    private Long personId;

    private Long roomId;
}
