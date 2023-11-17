package ro.itschool.Booking.entity;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class Status {

    private Long id;

    private String message;




    public Status(Long id , String message){
        this.message=message;
        this.id=id;
    }
}
