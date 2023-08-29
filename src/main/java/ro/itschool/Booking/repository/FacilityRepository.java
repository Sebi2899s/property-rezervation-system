package ro.itschool.Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import ro.itschool.Booking.entity.Facility;

public interface FacilityRepository extends JpaRepository<Facility,Long>, PagingAndSortingRepository<Facility,Long> {
}
