package app.controller;

import app.annotation.access.RestrictUserAccess;
import app.bean.TokenPool;
import app.constant.DoseBrand;
import app.constant.DoseStatus;
import app.constant.UserLevel;
import app.constant.VaccinationCenter;
import app.dao.AppointmentDao;
import app.model.Appointment;
import app.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    AppointmentDao appointmentDao;

    @Autowired
    private TokenPool tokenPool;

    @GetMapping(value = {"/index", "/"})
    public String index() {
        return "index";
    }

    @RestrictUserAccess(requiredLevel = UserLevel.CLIENT)
    @PostMapping("/create-appointment")
    @ResponseBody
    public String book(@RequestHeader("token") String token, @RequestBody Map<String, Object> form) throws ParseException {
        System.out.println(form.get("brand"));
        System.out.println(form.get("date"));
        System.out.println(form.get("center"));
        System.out.println(tokenPool.containsToken(token));
        System.out.println(tokenPool.getUserIdByToken(token));
        Appointment appointment = new Appointment();
        switch (form.get("brand").toString()){
            case "PFIZER":
                appointment.setDose_1_brand(DoseBrand.PFIZER);
                break;
            case "MODERNA":
                appointment.setDose_1_brand(DoseBrand.MODERNA);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + form.get("brand"));
        }
        Date sqlDate = convertDate((String) form.get("date"));
        appointment.setDose_1_date(sqlDate);
        switch (String.valueOf(form.get("center"))){
            case "UCD":
                appointment.setDose_1_center(VaccinationCenter.UCD);
                break;
            case "CITY_WEST":
                appointment.setDose_1_center(VaccinationCenter.CITY_WEST);
                break;
            case "CROKE_PARK":
                appointment.setDose_1_center(VaccinationCenter.CROKE_PARK);
            default:
                throw new IllegalStateException("Unexpected value: " + form.get("center"));
        }
        appointment.setDose_1_status(DoseStatus.PENDING);
        System.out.println(appointment);
        appointmentDao.save(appointment);
        return "OK";
    }

    private Date convertDate(String strDate) throws ParseException {
        LocalDate localDate = LocalDate.parse(strDate);
        java.sql.Date sqlDate = Date.valueOf(localDate);
        return sqlDate;
    }

}
