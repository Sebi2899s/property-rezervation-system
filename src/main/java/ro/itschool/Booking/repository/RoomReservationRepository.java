package ro.itschool.Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ro.itschool.Booking.entity.RoomReservation;

@Repository
public interface RoomReservationRepository extends JpaRepository<RoomReservation,Long>, PagingAndSortingRepository<RoomReservation,Long> {
}

