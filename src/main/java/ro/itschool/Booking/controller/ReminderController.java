package ro.itschool.Booking.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.entity.Reminder;
import ro.itschool.Booking.entity.Status;
import ro.itschool.Booking.service.ReminderService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/reminder")
public class ReminderController {
    @Autowired
    private ReminderService reminderService;

    @GetMapping(value = "/reminders")
    public List<Reminder> reminders(@RequestParam(defaultValue = "0") Integer pageNo,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(defaultValue = "ASC") String sortBy) {

        return reminderService.getReminders(pageNo, pageSize, sortBy);

    }

    @GetMapping
    public Reminder reminder(@RequestParam Long reminderId) throws IncorrectIdException {

        return reminderService.getReminderByIdOrThrow(reminderId);
    }

    @GetMapping(value = "/reminders-by-reservation")
    public List<Reminder> remindersByReservation(@RequestParam(defaultValue = "0") Integer pageNo,
                                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                                 @RequestParam(defaultValue = "ASC") String sortBy,
                                                 @PathVariable Long reservationId) {
        return reminderService.findAllByReservationSpecification(reservationId);
    }

    @Transactional
    @PostMapping(value = "/save")
    public Status saveReminder(@RequestParam Long reservationId) throws IncorrectIdException {

        Status status = new Status();
        Reminder reminder = reminderService.saveReminder(reservationId);

        status.setMessage("Reminder saved successfully !");
        status.setId(reminder.getId());

        return status;
    }

    @Transactional
    @PutMapping(value = "/update")
    public Status updateReminder(@RequestBody Reminder reminderRq, @RequestParam Long reservationId) throws IncorrectIdException {

        Status status = new Status();
        Reminder reminder = reminderService.updateReminder(reservationId, reminderRq);

        status.setMessage("Reminder updated successfully !");
        status.setId(reminder.getId());

        return status;
    }

    @Transactional
    @DeleteMapping(value = "/delete")
    public Status deleteReminder(@RequestParam Long reminderId) throws IncorrectIdException {
        Status status = new Status();

        reminderService.deleteReminder(reminderId);

        status.setMessage("Reminder deleted successfully !");
        status.setId(reminderId);
        return status;
    }
}
