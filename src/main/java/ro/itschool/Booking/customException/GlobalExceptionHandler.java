package ro.itschool.Booking.customException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IncorrectIdException.class)
    public ResponseEntity<ErrorDetails> handleIncorrectIdEx(IncorrectIdException incorrectIdException, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), incorrectIdException.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleIncorrectIdEx(PersonNotFoundException personNotFoundException, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), personNotFoundException.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InvalidMailException.class)
    public ResponseEntity<ErrorDetails> handleInvalidMailException(InvalidMailException invalidMailException, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), invalidMailException.getMessage(),request.getDescription(false));
        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IncorretNameException.class)
    public ResponseEntity<ErrorDetails> handleIncorrectNameEx(IncorretNameException incorretNameException, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), incorretNameException.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MobileNumberException.class)
    public ResponseEntity<ErrorDetails> handleMobileNumberEx(MobileNumberException mobileNumberException, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), mobileNumberException.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalEx(Exception exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_GATEWAY);
    }

}
