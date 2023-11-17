package ro.itschool.Booking.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.entity.Reminder;
import ro.itschool.Booking.entity.Reservation;
import ro.itschool.Booking.entity.Status;
import ro.itschool.Booking.repository.ReminderRepository;
import ro.itschool.Booking.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReminderService {
    @Autowired
    private ReminderRepository reminderRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ModelMapper mapper;

    public List<Reminder> getReminders(Integer pageNo,
                                       Integer pageSize,
                                       String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Reminder> pagedResult = reminderRepository.findAll(paging);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public List<Reminder> findAllByReservation(Integer pageNo, Integer pageSize, String sortBy, @NotNull Long reservation) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Reminder> pageByReservation = reminderRepository.findAll((Pageable) idEqualToReservationId(reservation));
        if (pageByReservation.hasContent()) {
            return pageByReservation.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public Specification<Reminder> idEqualToReservationId(Long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.<Reminder>get("reservation_id"), id);
    }


    public Reminder getReminderByIdOrThrow(@NotNull Long id) throws IncorrectIdException {
        Optional<Reminder> reminder = reminderRepository.findById(id);
        return reminder.orElseThrow(() -> new IncorrectIdException("This id " + id + " must not be null!"));
    }


    //reminder will be sent with two days before the reservation thats why dueDate is startDate - 2 days
    public Reminder saveReminder(Long reservationId) throws IncorrectIdException {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new IncorrectIdException("This reservation with id " + reservationId + " doesn't exists !"));
        LocalDate startDate = reservation.getCheckInDate();
        LocalDate dateDue = startDate.minusDays(2);
        Reminder reminder = Reminder.builder()
                .dateDue(dateDue)
                .body("Hi " + reservation.getPerson().getFirstName() + ",\n" +
                        "\n" +
                        "Just a friendly reminder that you have an upcoming reservation  to " + reservation.getProperty().getPropertyName() + ".\n" +
                        "\n" +
                        "Date: " + startDate + "\n" +
                        "Location: " + reservation.getProperty().getPropertyLocation() + "\n" +
                        "\n" +
                        "If you have any questions or concerns, please don’t hesitate to get in touch with us at " + reservation.getProperty().getPropertyEmail() + ".\n" +
                        "\n" +
                        "Thanks!")
                .action("Reservation reminder")
                .subject("Don't forget your reservation")
                .reservation(reservation)
                .build();

        return reminderRepository.save(reminder);


    }

    public Reminder updateReminder(Long reminderId, Reminder reminderRq) throws IncorrectIdException {
        Reminder reminder = getReminderByIdOrThrow(reminderId);
        mapper.map(reminderRq, reminder);

        return reminder;
    }

    public Long deleteReminder(Long reminderId) throws IncorrectIdException {

        getReminderByIdOrThrow(reminderId);

        reminderRepository.deleteById(reminderId);

        return reminderId;
    }
}
