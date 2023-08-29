package ro.itschool.Booking.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class FacilityRq {
    private String name;
    private String description;
    private Long facilityTypeId;
}
