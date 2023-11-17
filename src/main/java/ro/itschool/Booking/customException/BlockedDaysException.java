package ro.itschool.Booking.customException;

public class BlockedDaysException extends Exception{
    public BlockedDaysException(String message) {
        super(message);
    }
}
