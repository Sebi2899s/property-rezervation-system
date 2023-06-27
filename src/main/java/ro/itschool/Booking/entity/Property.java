package ro.itschool.Booking.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import ro.itschool.Booking.DtoEntity.PropertyDTO;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
//@Builder
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String propertyType;

    private String propertyName;


    private String propertyEmail;

    private String propertyLocation;

    private String propertyAddress;

    @JsonManagedReference
    @OneToMany(mappedBy = "property")
    @ToString.Exclude
    private List<Person> personList;

    public Property(Long id, String propertyType, String propertyName, String propertyEmail, String propertyLocation, String propertyAddress) {
        this.id = id;
        this.propertyType = propertyType;
        this.propertyName = propertyName;
        this.propertyEmail = propertyEmail;
        this.propertyLocation = propertyLocation;
        this.propertyAddress = propertyAddress;
    }


    public PropertyDTO toDTO() {
        return new PropertyDTO(id, propertyType, propertyName, propertyLocation, propertyAddress, propertyEmail);
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
    public static PropertyBuilder builder(){
        return new PropertyBuilder();
    }
//implementing Builder Design Pattern for Property class
    public static class PropertyBuilder {
        private Property property;

        private PropertyBuilder() {
            this.property = new Property();
        }
        public PropertyBuilder id(Long id){
            this.property.id=id;
            return this;
        }
        public PropertyBuilder propertyType(String propertyType){
            this.property.propertyType=propertyType;
            return this;
        }
        public PropertyBuilder propertyName(String propertyName){
            this.property.propertyName=propertyName;
            return this;
        }
        public PropertyBuilder propertyEmail(String propertyEmail){
            this.property.propertyEmail=propertyEmail;
            return this;
        }
        public PropertyBuilder propertyLocation(String propertyLocation){
            this.property.propertyLocation=propertyLocation;
            return this;
        }
        public PropertyBuilder propertyAddress(String propertyAddress){
            this.property.propertyAddress=propertyAddress;
            return this;
        }
        public Property build(){
            return this.property;
        }

    }
}
