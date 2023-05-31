package ro.itschool.Booking.customException;

public class PropertyNotFoundException extends Exception{
    public PropertyNotFoundException(String message){
        super(message);
    }
}
