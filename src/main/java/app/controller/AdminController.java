package app.controller;

import app.annotation.access.RestrictUserAccess;
import app.constant.UserLevel;
import app.dao.AppointmentDao;
import app.vo.AdminAppointment;
import app.vo.UpdateForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    AppointmentDao appointmentDao;

    @RestrictUserAccess(requiredLevel = UserLevel.ADMIN)
    @GetMapping
    public String adminPage(@RequestHeader("token") String token, Model model) {
        List<AdminAppointment> appointments = appointmentDao.findAllPendingAppointments();
        model.addAttribute("appointments_number", appointments.size());
        model.addAttribute("appointments", appointments);
        return "admin";
    }

    @RestrictUserAccess(requiredLevel = UserLevel.ADMIN)
    @PostMapping("/update-appointment")
    @ResponseBody
    public String updateAppointment(@RequestHeader("token") String token, @RequestBody UpdateForm form) {
        if(!appointmentDao.existsById(form.getAppointmentId())) return "Invalid appointment id.";
        Long id = form.getAppointmentId();
        if(form.getDoseNumber() == 1) {
            log.info("Update: appointment id = " + id);
            appointmentDao.updateDose1(id);
        }
        else if(form.getDoseNumber() == 2) {
            log.info("Update: appointment id = " + id);
            appointmentDao.updateDose2(id);
        }
        else {
            log.warn("Failed to update appointment with appointment_id = " + id + " and dose number = " + form.getDoseNumber());
            return "Invalid dose number.";
        }
        return "Approved.";
    }

}
