package ro.itschool.Booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

public class GlobalExceptionHandler {
    @ExceptionHandler(IncorrectIdException.class)
    public ResponseEntity<?> handleIncorrectIdEx(IncorrectIdException incorrectIdException, WebRequest request){
        ErrorDetails errorDetails =new ErrorDetails(new Date(), incorrectIdException.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(IncorretNameException.class)
    public ResponseEntity<?> handleIncorrectNameEx(IncorretNameException incorretNameException, WebRequest request){
        ErrorDetails errorDetails =new ErrorDetails(new Date(), incorretNameException.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MobileNumberException.class)
    public ResponseEntity<?> handleMobileNumberEx(MobileNumberException mobileNumberException, WebRequest request){
        ErrorDetails errorDetails =new ErrorDetails(new Date(), mobileNumberException.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalEx(Exception exception, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_GATEWAY);
    }

}
