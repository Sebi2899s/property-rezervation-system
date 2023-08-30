package ro.itschool.Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ro.itschool.Booking.entity.Facility;
@Repository
public interface FacilityRepository extends JpaRepository<Facility,Long>, PagingAndSortingRepository<Facility,Long> {
}
