package ro.itschool.Booking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ro.itschool.Booking.Dto.PropertyDTO;
import ro.itschool.Booking.util.CloneProperty;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Builder
public class Property implements CloneProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String propertyType;

    private String propertyName;

    private Double price;
    private String propertyEmail;

    private String propertyLocation;

    private String propertyAddress;
    private String description;

    private boolean hasBreakfast;
    private double breakfastCost;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private List<LocalDate> blockedDates;

    @JsonManagedReference
    @OneToMany(mappedBy = "property")
    @ToString.Exclude
    private List<Person> personList;
    @OneToMany(mappedBy = "property")
    @JsonIgnore
    @ToString.Exclude
    private List<Reservation> reservations;

    public Property(Long id, String propertyType, String propertyName, String propertyEmail, String propertyLocation, String propertyAddress, Double price,String description,boolean hasBreakfast) {
        this.id = id;
        this.propertyType = propertyType;
        this.propertyName = propertyName;
        this.propertyEmail = propertyEmail;
        this.propertyLocation = propertyLocation;
        this.propertyAddress = propertyAddress;
        this.price = price;
        this.description=description;
        this.hasBreakfast=hasBreakfast;
    }

    public Property(Property property) {

    }


    public PropertyDTO toDTO() {
        return new PropertyDTO(id, propertyType, propertyName, propertyLocation, propertyAddress, propertyEmail, price,description);
    }

    @Override
    public String toString() {
        return "Property{" +
                "id=" + id +
                ", propertyType='" + propertyType + '\'' +
                ", propertyName='" + propertyName + '\'' +
                ", propertyEmail='" + propertyEmail + '\'' +
                ", propertyLocation='" + propertyLocation + '\'' +
                ", propertyAddress='" + propertyAddress + '\'' +
                ", personList=" + personList +

                '}';
    }

    public static PropertyBuilder builder() {
        return new PropertyBuilder();
    }

    //implement Prototype Design Pattern that clone objects
    @Override
    public Property clone() {
        return new Property(this);
    }

    //implementing Builder Design Pattern for Property class
    public static class PropertyBuilder {
        private Property property;

        private PropertyBuilder() {
            this.property = new Property();
        }

        public PropertyBuilder id(Long id) {
            this.property.id = id;
            return this;
        }

        public PropertyBuilder propertyType(String propertyType) {
            this.property.propertyType = propertyType;
            return this;
        }

        public PropertyBuilder propertyName(String propertyName) {
            this.property.propertyName = propertyName;
            return this;
        }

        public PropertyBuilder propertyEmail(String propertyEmail) {
            this.property.propertyEmail = propertyEmail;
            return this;
        }

        public PropertyBuilder propertyLocation(String propertyLocation) {
            this.property.propertyLocation = propertyLocation;
            return this;
        }

        public PropertyBuilder propertyAddress(String propertyAddress) {
            this.property.propertyAddress = propertyAddress;
            return this;
        }

        public Property build() {
            return this.property;
        }

    }

    public List<LocalDate> getBlockedDates() {
        return blockedDates;
    }

    public void setBlockedDates(List<LocalDate> blockedDates) {
        this.blockedDates = blockedDates;
    }
}
