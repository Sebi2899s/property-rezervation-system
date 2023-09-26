package ro.itschool.Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.itschool.Booking.entity.Room;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long>, PagingAndSortingRepository<Room,Long>{
    @Query(value = "SELECT * FROM Room r WHERE r.id NOT IN (" +
            "    SELECT res.room_id FROM Reservation res WHERE (" +
            "        res.checkInDate <= :checkout AND res.checkOutDate >= :checkin" +
            "    )" +
            ")", nativeQuery = true)
    List<Room> findAvailableRooms(@Param("checkin") LocalDate checkIn, @Param("checkout") LocalDate checkOut);
}

