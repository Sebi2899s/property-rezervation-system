package ro.itschool.Booking.specifications;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.entity.Property;

public class Specifications {
    public static Specification<Property> getPropertyByPersonFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> {
            Join<Property, Person> propertyPersonJoin = root.join("firstName");
            return criteriaBuilder.equal(propertyPersonJoin.get("firstName"), firstName);
        };
    }

    public static Specification<Property> getPropertyByPropertyType(String propertyType) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("propertyType"), propertyType);
        };
    }
}
