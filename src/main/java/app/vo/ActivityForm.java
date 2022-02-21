package app.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityForm {
    private String first_dose_center;
    private Date first_dose_date;
    private String first_dose_status;
    private String second_dose_center;
    private Date second_dose_date;
    private String second_dose_status;
}

