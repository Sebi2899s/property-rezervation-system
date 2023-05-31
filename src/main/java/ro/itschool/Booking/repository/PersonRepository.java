package ro.itschool.Booking.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.entity.Property;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {

    Optional<Object> getPersonByMobileNumber(String mobileNumber);

    Optional<Person> findByEmail(String email);

    @Query("SELECT p.firstName, p.lastName FROM person p WHERE (:firstName is null or u.firstName = :firstName))" +
            "AND (:lastName is null or u.lastName = :lastName)")
    List<Person> searchByMailOrUsername(String firstName,
                                         String lastName);
}
