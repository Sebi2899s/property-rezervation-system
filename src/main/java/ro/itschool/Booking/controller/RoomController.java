package ro.itschool.Booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.itschool.Booking.Dto.RoomRequest;
import ro.itschool.Booking.Dto.RoomReservationRq;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.entity.Room;
import ro.itschool.Booking.entity.RoomReservation;
import ro.itschool.Booking.service.RoomService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping(value = "/all")
    public List<Room> findAllRoomReservationWithPagination(@RequestParam(defaultValue = "0") Integer pageNo,
                                                           @RequestParam(defaultValue = "10") Integer pageSize,
                                                           @RequestParam(defaultValue = "ASC") String sortBy) {

        return roomService.allRooms(pageNo, pageSize, sortBy);
    }
    @GetMapping()
    public Room getRoomReservationById(@RequestParam Long id) throws IncorrectIdException {
        return roomService.getRoomById(id);
    }


    @PostMapping(value = "/save")
    public Room saveRoomReservation(@RequestBody Room room){
        return roomService.saveRoom(room);
    }

    @PutMapping(value = "/update")
    public Room updateRoomReservation(@RequestParam Long id, @RequestBody RoomRequest roomRequest) throws IncorrectIdException {
        return roomService.updateRoom(id,roomRequest);
    }

    @DeleteMapping(value = "/delete")
    public Long deleteRoomReservation(@RequestParam Long id) throws IncorrectIdException {
        return roomService.deleteRoom(id);
    }
}
