package ro.itschool.Booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.entity.Room;
import ro.itschool.Booking.repository.RoomRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public List<Room> allRooms() {
        List<Room> allRooms = roomRepository.findAll();
        if (allRooms == null) {
            return new ArrayList<>();
        }
        return allRooms;
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

    public Room updateRoom(Long id, Room roomRq) {
        return null;
    }


}
