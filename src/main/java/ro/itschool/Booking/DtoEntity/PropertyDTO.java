package ro.itschool.Booking.DtoEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.entity.Property;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyDTO {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Long id;

    private String propertyType;

    private String propertyName;

    private String propertyEmail;

    private String propertyPassword;

    private String propertyLocation;

    private String propertyAddress;

    public PropertyDTO(Long id, String propertyType, String propertyName, String propertyLocation, String propertyAddress, String propertyEmail) {
        this.id = id;
        this.propertyType = propertyType;
        this.propertyName = propertyName;
        this.propertyLocation = propertyLocation;
        this.propertyAddress = propertyAddress;
        this.propertyEmail = propertyEmail;
    }

}
