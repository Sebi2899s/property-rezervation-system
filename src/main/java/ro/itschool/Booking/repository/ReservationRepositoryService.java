package ro.itschool.Booking.repository;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import ro.itschool.Booking.Dto.ReservationRequestDTO;
import ro.itschool.Booking.entity.Reservation;

public interface ReservationRepositoryService {
    public Reservation updateOrSaveReservation(@NotNull ReservationRequestDTO reservationRequestDTO, @Nullable Long reservationId);
}
