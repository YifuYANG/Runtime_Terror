package app.model;

import app.constant.DoseBrand;
import app.constant.DoseStatus;
import app.constant.VaccinationCenter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointment_id;

    @NotNull
    private Long user_id;

    private VaccinationCenter dose_1_center;
    private VaccinationCenter dose_2_center;
    private DoseStatus dose_1_status;
    private DoseStatus dose_2_status;
    private Date dose_1_date;
    private Date dose_2_date;
    private DoseBrand dose_1_brand;
    private DoseBrand dose_2_brand;

    public String toString(){
        return "Dose Brand: " + dose_1_brand +" Dose date: " + dose_1_date + " Dose centre: " + dose_1_center;
    }
}
