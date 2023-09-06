package ro.itschool.Booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ro.itschool.Booking.Dto.PersonDTO;
import ro.itschool.Booking.Dto.RoomReservationRq;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.entity.Reminder;
import ro.itschool.Booking.entity.Room;
import ro.itschool.Booking.entity.RoomReservation;
import ro.itschool.Booking.repository.RoomReservationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomReservationService {
    @Autowired
    private RoomReservationRepository roomReservationRepository;
    @Autowired
    private PersonService personService;
    @Autowired
    private RoomService roomService;

    public List<RoomReservation> allRoomReservations(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<RoomReservation> pagedResult = roomReservationRepository.findAll(paging);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public RoomReservation getRoomReservationById(Long id) {
        return roomReservationRepository.findById(id).get();
    }

    public RoomReservation saveRoomReservation(RoomReservation roomReservation) {
        if (roomReservation == null) {
            throw new RuntimeException("This roomReservation cannot be null ");
        }
        return roomReservationRepository.save(roomReservation);
    }

    public RoomReservation updateRoomReservation(Long id, RoomReservationRq roomReservationRq) throws IncorrectIdException {
        if (id == null) {
            throw new IncorrectIdException("This id must not be null");
        }
        RoomReservation roomReservation = getRoomReservationById(id);
        Person person = personService.getByIdPerson(roomReservationRq.getPersonId());
        Room room = roomService.getRoomById(roomReservationRq.getRoomId());
        roomReservation.setPerson(person);
        roomReservation.setRoom(room);
        return roomReservationRepository.save(roomReservation);
    }

    public Long deleteRoomReservation(Long id) throws IncorrectIdException {
        if (id == null) {
            throw new IncorrectIdException("This id must not be null");
        }
        roomReservationRepository.deleteById(id);
        return id;
    }
}
