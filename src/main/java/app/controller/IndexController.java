package app.controller;

import app.annotation.access.RestrictUserAccess;
import app.bean.TokenPool;
import app.constant.*;
import app.dao.AppointmentDao;
import app.exception.CustomErrorException;
import app.model.Appointment;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
@Slf4j
public class IndexController {

    @Value("classpath:vega-lite/brand-popularity.json")
    private Resource dosePopularityFile;

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
    public String book(@RequestHeader("token") String token, @RequestBody Map<String, Object> form) throws ParseException, CustomErrorException {
        try {
            Appointment appointment = new Appointment();

            if(appointmentDao.findByUserId(tokenPool.getUserIdByToken(token))!=null){
                return "You have already booked your first appointment, " +
                        "please book your second appointment" +
                        ", or go to activities page to view details";
            }

            appointment.setUser_id(tokenPool.getUserIdByToken(token));
            /**Vaccination Brand*/
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
            /**Vaccination Date and Slot validation*/
            Date sqlDate = convertDate((String) form.get("date"));
            if(appointmentDao.findAllByDate1(sqlDate).size()>=3){
                return "All slots taking for this date please try another!";
            }
            appointment.setDose_1_date(sqlDate);

            String slot_string = form.get("slot").toString();
            DoseSlot doseSlot = setSlot(slot_string);

            Appointment exists = appointmentDao.findAppointmentByDateAndSlot(sqlDate,doseSlot);

            //try to replace this with an sql statement that returns true if t exists
            if (exists==null) {
                appointment.setDose_1_Slot(doseSlot);
            }else{
                return "This slot is taken for this date.";
            }

            /** Vaccination Center*/
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

            appointmentDao.save(appointment);
            return "Thank you for booking your first appointment";
        } catch (Exception e){
            throw new CustomErrorException("some error happened");
        }

    }

    @RestrictUserAccess(requiredLevel = UserLevel.CLIENT)
    @PostMapping("/create-second-appointment")
    @ResponseBody
    public String bookSecondAppointment(@RequestHeader("token") String token, @RequestBody Map<String, Object> form) throws ParseException, CustomErrorException {

        try {
            /** Initial validation checks for second appointment*/
            if(appointmentDao.findByUserId(tokenPool.getUserIdByToken(token))==null){
                log.warn("User ID = " + tokenPool.getUserIdByToken(token) + ": Must book first appointment");
                return "You must book your first appointment before your second " +
                        "appointment. Please book your first appointment";
            }
            Appointment appointment = appointmentDao.findByUserId(tokenPool.getUserIdByToken(token));
            if(appointment.getDose_1_status()==DoseStatus.PENDING){
                return "Dose status is pending, please return later to book again";
            }
            if(appointment.getDose_2_status()==DoseStatus.PENDING){
                return "Dose status is pending, please go to activities page to view details";
            }
            else if(appointment.getDose_2_status()==DoseStatus.RECEIVED){
                return "You have received the vaccination, thank you for using our services!";
            }

            /** Vaccination brand*/
            switch (form.get("brand").toString()){
                case "PFIZER":
                    appointment.setDose_2_brand(DoseBrand.PFIZER);
                    break;
                case "MODERNA":
                    appointment.setDose_2_brand(DoseBrand.MODERNA);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + form.get("brand"));
            }

            /**Vaccination Date and Slot validation*/
            Date sqlDate = convertDate((String) form.get("date"));
            if(appointmentDao.findAllByDate2(sqlDate).size()>=3){

                return "All slots taking for this date please try another!";
            }

            Date first_date = appointment.getDose_1_date();
            Date second_date = sqlDate;
            long diffInMill = Math.abs(second_date.getTime()-first_date.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMill, TimeUnit.MILLISECONDS);
            if (diff < 21) {
                return "There must be three weeks between your first and second appointment!";
            }
            appointment.setDose_2_date(sqlDate);

            String slot_string = form.get("slot").toString();
            DoseSlot slot = setSlot(slot_string);

            Appointment exists = appointmentDao.findSecondAppointmentByDateAndSlot(sqlDate,slot);
            //try to replace this with a sql statement that returns true if t exists
            if (exists==null) {
                appointment.setDose_2_slot(slot);
            }else{
                return "This slot is taken for this date.";
            }

            /** Vaccination Center*/
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

            appointmentDao.save(appointment);
            log.info("User ID = " + tokenPool.getUserIdByToken(token) + ": booked 2nd appointment successfully.");
            return "Thank you for booking your second appointment";
        } catch (Exception e){
            throw new CustomErrorException("some error happened");
        }

    }

    @GetMapping("/vis/dose-popularity")
    @ResponseBody
    public String visualizeDosePopularity() throws CustomErrorException {
        try {
            Gson gson = new Gson();
            String content = Files.readString(dosePopularityFile.getFile().toPath());
            Map<?, ?> object = gson.fromJson(content, Map.class);
            List<Map<Object, Object>> values = (List<Map<Object, Object>>) ((Map<Object, Object>)object.get("data")).get("values");
            for(Map<Object, Object> value : values) {
                if(value.get("Brand").equals("Moderna")) value.put("Frequency", appointmentDao.getNumberOfModerna());
                else value.put("Frequency", appointmentDao.getNumberOfPfizer());
            }
            return gson.toJson(object);
        } catch (Exception e){
            throw new CustomErrorException("some error happened");
        }
    }

    private Date convertDate(String strDate) {
        LocalDate localDate = LocalDate.parse(strDate);
        java.sql.Date sqlDate = Date.valueOf(localDate);
        return sqlDate;
    }

    private DoseSlot setSlot(String slot_string){
        DoseSlot doseSlot;
        switch (slot_string){
            case "MORNING":
                doseSlot = DoseSlot.MORNING;
                break;
            case "AFTERNOON":
                doseSlot = DoseSlot.AFTERNOON;
                break;
            case "EVENING":
                doseSlot = DoseSlot.EVENING;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + slot_string);
        }
        return doseSlot;
    }
}