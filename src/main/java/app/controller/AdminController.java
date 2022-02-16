package app.controller;

import app.annotation.access.RestrictUserAccess;
import app.constant.UserLevel;
import app.dao.AppointmentDao;
import app.vo.AdminAppointment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
