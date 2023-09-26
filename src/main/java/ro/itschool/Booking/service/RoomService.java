package ro.itschool.Booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ro.itschool.Booking.Dto.RoomRequest;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.entity.Reminder;
import ro.itschool.Booking.entity.Room;
import ro.itschool.Booking.entity.RoomReservation;
import ro.itschool.Booking.repository.RoomRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    @Lazy
    private RoomReservationService roomReservationService;

    public List<Room> getAllRoomsByAvailability(LocalDate checkIn, LocalDate checkOut){
        return roomRepository.findAvailableRooms(checkIn,checkOut);
    }
    public List<Room> allRooms(Integer pageNo,
                               Integer pageSize,
                               String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Room> pagedResult = roomRepository.findAll(paging);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();

        }
    }

    public Room getRoomById(Long id) throws IncorrectIdException {
        if (id == null) {
            throw new IncorrectIdException("This id must not be null");
        }
        return roomRepository.findById(id).get();

    }

    public Room saveRoom(Room room) {
        if (room == null) {
            throw new RuntimeException("This room cannot be null");
        }
        return roomRepository.save(room);

    }

    public Room updateRoom(Long id, RoomRequest roomRq) throws IncorrectIdException {
        Room room = getRoomById(id);
        RoomReservation roomReservation = roomReservationService.getRoomReservationById(roomRq.getRoomReservationId());
        List<RoomReservation> roomReservationList = new ArrayList<>();
        roomReservationList.add(roomReservation);
        room.setRoomReservations(roomReservationList);
        room.setType(roomRq.getType());
        room.setDescription(roomRq.getDescription());
        room.setBookingEnabled(roomRq.isBookingEnabled());
        return saveRoom(room);
    }

    public Long deleteRoom(Long id) throws IncorrectIdException {
        if (id == null) {
            throw new IncorrectIdException("This id must not be null");
        }
        roomRepository.deleteById(id);
        return id;
    }
}
