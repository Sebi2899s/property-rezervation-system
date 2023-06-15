package ro.itschool.Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.entity.Property;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long>, JpaSpecificationExecutor<Property>, PagingAndSortingRepository<Property, Long> {

    List<Property> findByPropertyName(String name);


    Optional<Property> getPropertyByPropertyEmail(String propertyName);

    Optional<Property> findByPropertyEmail(String email);

    @Query(value = "SELECT property.name FROM Property WHERE property.name=%name%", nativeQuery = true)
    Optional<List<Property>> getPropertyNameAndFilter(String name);

    @Query(value = "SELECT p.propertyName, p.propertyEmail FROM property p WHERE (:propertyName is null or p.propertyName = :propertyName))" +
            "AND (:propertyEmail is null or p.propertyEmail = :propertyEmail)", nativeQuery = true)
    List<Property> searchPropertyNameOrPropertyEmail(String propertyName,
                                                     String propertyEmail,
                                                     Integer page,
                                                     Integer pageSize,
                                                     String sortBy);
}
