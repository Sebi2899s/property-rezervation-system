package ro.itschool.Booking.customException;

public class InvalidMailException extends Exception{
    public InvalidMailException(String message){
        super(message);
    }
}
