package ro.itschool.Booking.DtoEntity;

import ro.itschool.Booking.entity.Person;
public class ReservationRequestDTO {
    private Long personId;
    private String checkIn;
    private String checkOut;
    private Long propertyId;


    public ReservationRequestDTO() {
    }


    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }
}
