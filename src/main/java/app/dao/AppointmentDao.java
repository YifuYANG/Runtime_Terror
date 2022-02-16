package app.dao;

import app.constant.DoseStatus;
import app.model.Appointment;
import app.repository.AppointmentRepository;
import app.vo.AdminAppointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentDao {

    @Autowired
    AppointmentRepository appointmentRepository;

    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public Appointment findByUserId(Long id) {
        return appointmentRepository.findByUserId(id);
    }

    public List<AdminAppointment> findAllPendingAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        List<AdminAppointment> results = new ArrayList<>();
        for(Appointment appointment : appointments) {
            if(hasPendingFirstDose(appointment)) results.add(fetchAppointmentInfo(1, appointment));
            else if(hasPendingSecondDose(appointment)) results.add(fetchAppointmentInfo(2, appointment));
        }
        return results;
    }

    public boolean existsByUserId(Long userId) {
        return appointmentRepository.findByUserId(userId) != null;
    }

    private boolean hasPendingFirstDose(Appointment appointment) {
        return appointment.getDose_1_status() == DoseStatus.PENDING;
    }

    private boolean hasPendingSecondDose(Appointment appointment) {
        return appointment.getDose_2_status() == DoseStatus.PENDING;
    }

    private AdminAppointment fetchAppointmentInfo(int doseNumber, Appointment appointment) {
        AdminAppointment adminAppointment = new AdminAppointment();
        if(doseNumber == 1) {
            adminAppointment.setBrand(appointment.getDose_1_brand().name());
            adminAppointment.setUserId(appointment.getUser_id());
            adminAppointment.setDate(appointment.getDose_1_date());
            adminAppointment.setDoseNumber(doseNumber);
            adminAppointment.setId(appointment.getId());
            adminAppointment.setStatus(appointment.getDose_1_status().name());
            adminAppointment.setVaccinationCenter(appointment.getDose_1_center().name());
        } else {
            adminAppointment.setBrand(appointment.getDose_2_brand().name());
            adminAppointment.setUserId(appointment.getUser_id());
            adminAppointment.setDate(appointment.getDose_2_date());
            adminAppointment.setDoseNumber(doseNumber);
            adminAppointment.setId(appointment.getId());
            adminAppointment.setStatus(appointment.getDose_2_status().name());
            adminAppointment.setVaccinationCenter(appointment.getDose_2_center().name());
        }
        return adminAppointment;
    }

}
