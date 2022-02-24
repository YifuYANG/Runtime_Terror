package app.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminAppointment {

    private Long id; //Appointment id
    private Long userId;
    private int doseNumber;
    private String brand;
    private String vaccinationCenter;
    private String status;
    private Date date;


}
