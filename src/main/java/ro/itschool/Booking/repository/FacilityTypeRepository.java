package ro.itschool.Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import ro.itschool.Booking.entity.FacilityType;

public interface FacilityTypeRepository extends JpaRepository<FacilityType, Long>, PagingAndSortingRepository<FacilityType, Long> {
}
