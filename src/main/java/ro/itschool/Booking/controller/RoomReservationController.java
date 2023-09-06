package ro.itschool.Booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.itschool.Booking.Dto.RoomReservationRq;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.entity.RoomReservation;
import ro.itschool.Booking.service.RoomReservationService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/room-reservation")
public class RoomReservationController {
    @Autowired
    private RoomReservationService roomReservationService;


    @GetMapping(value = "/all")
    public List<RoomReservation> findAllRoomReservationWithPagination(@RequestParam(defaultValue = "0") Integer pageNo,
                                                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                                                      @RequestParam(defaultValue = "ASC") String sortBy) {

        return roomReservationService.allRoomReservations(pageNo, pageSize, sortBy);
    }

    @GetMapping()
    public RoomReservation getRoomReservationById(@RequestParam Long id){
        return roomReservationService.getRoomReservationById(id);
    }


    @PostMapping(value = "/save")
    public RoomReservation saveRoomReservation(@RequestBody RoomReservation roomReservation){
        return roomReservationService.saveRoomReservation(roomReservation);
    }

    @PutMapping(value = "/update")
    public RoomReservation updateRoomReservation(@RequestParam Long id, @RequestBody RoomReservationRq roomReservationRq) throws IncorrectIdException {
        return roomReservationService.updateRoomReservation(id,roomReservationRq);
    }

    @DeleteMapping(value = "/delete")
    public Long deleteRoomReservation(@RequestParam Long id) throws IncorrectIdException {
        return roomReservationService.deleteRoomReservation(id);
    }
}
