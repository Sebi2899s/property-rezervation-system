package ro.itschool.Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ro.itschool.Booking.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long>, PagingAndSortingRepository<Room,Long>{
}
