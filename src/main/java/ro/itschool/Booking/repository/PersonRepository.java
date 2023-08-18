package ro.itschool.Booking.repository;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ro.itschool.Booking.Dto.ReservationRequestDTO;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.entity.Reservation;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person>, PagingAndSortingRepository<Person,Long> {

    Optional<Object> getPersonByMobileNumber(String mobileNumber);

    Optional<Person> findByEmail(String email);


}
