package ro.itschool.Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ro.itschool.Booking.entity.FacilityType;
@Repository
public interface FacilityTypeRepository extends JpaRepository<FacilityType, Long>, PagingAndSortingRepository<FacilityType, Long> {
}
