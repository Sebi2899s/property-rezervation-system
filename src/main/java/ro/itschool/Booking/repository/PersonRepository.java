package ro.itschool.Booking.repository;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.itschool.Booking.Dto.ReservationRequestDTO;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.entity.Property;
import ro.itschool.Booking.entity.Reservation;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person>, PagingAndSortingRepository<Person, Long> {

    Optional<Object> getPersonByMobileNumber(String mobileNumber);

    Optional<Person> findByEmail(String email);

    // will find all persons that have reservation on a property based on property name
    @Query(value = "SELECT * FROM person " +
            "JOIN property ON property.id = person.property_id " +
            "WHERE property.property_name = :propertyName",nativeQuery = true)
    List<Person> getAllPersonsByPropertiesName(String propertyName);
    @Query(value = "SELECT * FROM person WHERE LOWER(person.first_name) LIKE LOWER(%:name%)", nativeQuery = true)
    List<Person> getPersonsByFirstName(@Param("name") String name);

}
