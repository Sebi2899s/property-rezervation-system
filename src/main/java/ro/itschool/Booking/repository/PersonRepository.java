package ro.itschool.Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import ro.itschool.Booking.entity.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person>, PagingAndSortingRepository<Person,Long> {

    Optional<Object> getPersonByMobileNumber(String mobileNumber);

    Optional<Person> findByEmail(String email);

    @Query(value = "SELECT p.firstName, p.lastName FROM person p WHERE (:firstName is null or p.firstName = :firstName))" +
            "AND (:lastName is null or p.lastName = :lastName)",nativeQuery = true)
    List<Person> searchFirstNameOrLastName(String firstName,
                                           String lastName,
                                          Integer pageNo,
                                           Integer pageSize,
                                           String sortBy);
}
