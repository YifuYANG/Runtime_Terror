package app.controller;

import app.constant.DoseBrand;
import app.constant.DoseStatus;
import app.constant.VaccinationCenter;
import app.vo.AdminAppointment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String adminPage(Model model) {
        List<AdminAppointment> appointments = new ArrayList<>();
        AdminAppointment adminAppointment = new AdminAppointment(1L, 2L, 1,
                DoseBrand.PFIZER.name(),
                VaccinationCenter.UCD.name(),
                DoseStatus.PENDING.name(),
                new Date()
                );
        appointments.add(adminAppointment);
        model.addAttribute("appointments_number", 1);
        model.addAttribute("appointments", appointments);
        return "admin";
    }

}
