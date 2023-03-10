package ro.itschool.Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.itschool.Booking.entity.Property;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByPropertyName(String name);


    Optional<Property> getPropertyByPropertyEmail(String propertyName);

    Optional<Property> findByPropertyEmail(String email);
}
