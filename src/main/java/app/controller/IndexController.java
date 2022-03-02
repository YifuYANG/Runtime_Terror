package app.controller;

import app.annotation.access.RestrictUserAccess;
import app.bean.TokenPool;
import app.constant.*;
import app.dao.AppointmentDao;
import app.model.Appointment;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.ParseException;
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

    @GetMapping(value = "/appointment2")
    public String appointment2() {return "appointment2";}

    @RestrictUserAccess(requiredLevel = UserLevel.CLIENT)
    @PostMapping("/create-appointment")
    @ResponseBody
    public String book(@RequestHeader("token") String token, @RequestBody Map<String, Object> form) throws ParseException {
        Appointment appointment = new Appointment();

        if(appointmentDao.findByUserId(tokenPool.getUserIdByToken(token))!=null){
            System.out.println("first appointment already booked");
            return "You have already booked your first appointment, " +
                    "please book your second appointment";
        }

        appointment.setUser_id(tokenPool.getUserIdByToken(token));

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
        if(appointmentDao.findAllByDate1(sqlDate).size()>=3){
            System.out.println("all slots taking on this date");
            return "All slots taking for this date please try another!";
        }
        appointment.setDose_1_date(sqlDate);

        System.out.println("----------"+form.get("slot").toString());

        //String slot = form.get("slot").toString();
        DoseSlot slot = DoseSlot.MORNING;
        System.out.println(slot);
        switch (form.get("slot").toString()){
            case "MORNING":
                slot = DoseSlot.MORNING;
                break;
            case "AFTERNOON":
                slot = DoseSlot.AFTERNOON;
                break;
            case "EVENING":
                slot = DoseSlot.EVENING;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + form.get("brand"));
        }

        System.out.println(slot);
        Appointment exists = appointmentDao.findAppointmentByDateAndSlot(sqlDate,slot);
        System.out.println(exists);
        //try to replace this with an sql statement that returns true if t exists
        if (exists==null) {
            appointment.setDose_1_Slot(slot);
        }else{
            return "This slot is taken for this date.";
        }


        switch (String.valueOf(form.get("center"))){
            case "UCD":
                appointment.setDose_1_center(VaccinationCenter.UCD);
                break;
            case "CITY_WEST":
                appointment.setDose_1_center(VaccinationCenter.CITY_WEST);
                break;
            case "CROKE_PARK":
                appointment.setDose_1_center(VaccinationCenter.CROKE_PARK);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + form.get("center"));
        }
        appointment.setDose_1_status(DoseStatus.PENDING);
        System.out.println(appointment);
        appointmentDao.save(appointment);
        return "OK";
    }

    @RestrictUserAccess(requiredLevel = UserLevel.CLIENT)
    @PostMapping("/create-second-appointment")
    @ResponseBody
    public String bookSecondAppointment(@RequestHeader("token") String token, @RequestBody Map<String, Object> form) throws ParseException {
        System.out.println(tokenPool.getUserIdByToken(token));

        if(appointmentDao.findByUserId(tokenPool.getUserIdByToken(token))==null){
            System.out.println("Must book first appointment");
            return "You must book your first appointment before your second " +
                    "appointment. Please book your first appointment";
        }
        Appointment appointment = appointmentDao.findByUserId(tokenPool.getUserIdByToken(token));
        if(appointment.getDose_1_status()==DoseStatus.PENDING){
            return "Dose status is pending, please return later to book again";
        }

        appointment.setDose_2_brand(appointment.getDose_1_brand());

        Date sqlDate = convertDate((String) form.get("date"));
        appointment.setDose_2_date(sqlDate);

        switch (String.valueOf(form.get("center"))){
            case "UCD":
                appointment.setDose_2_center(VaccinationCenter.UCD);
                break;
            case "CITY_WEST":
                appointment.setDose_2_center(VaccinationCenter.CITY_WEST);
                break;
            case "CROKE_PARK":
                appointment.setDose_2_center(VaccinationCenter.CROKE_PARK);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + form.get("center"));
        }

        appointment.setDose_2_status(DoseStatus.PENDING);
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
