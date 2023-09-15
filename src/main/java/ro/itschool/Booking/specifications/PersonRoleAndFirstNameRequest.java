package ro.itschool.Booking.specifications;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ro.itschool.Booking.entity.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonRoleAndFirstNameRequest {

    String name;
    String mobileNumber;
}
