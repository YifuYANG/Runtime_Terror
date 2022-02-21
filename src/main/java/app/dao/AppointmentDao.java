package app.dao;

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
    private AppointmentRepository appointmentRepository;

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public Appointment findByUserId(Long id) {
        return appointmentRepository.findByUserId(id);
    }

    public List<AdminAppointment> findAllPendingAppointments() {
        List<AdminAppointment> results = new ArrayList<>();
        List<Appointment> firstDoseAppointments = appointmentRepository.findPendingFirstDose();
        List<Appointment> secondDoseAppointments = appointmentRepository.findPendingSecondDose();
        //Format 1st list
        for(Appointment a : firstDoseAppointments) {
            AdminAppointment result = new AdminAppointment();
            result.setId(a.getAppointment_id());
            result.setUserId(a.getUser_id());
            result.setVaccinationCenter(a.getDose_1_center().name());
            result.setDate(a.getDose_1_date());
            result.setDoseNumber(1);
            result.setBrand(a.getDose_1_brand().name());
            results.add(result);
        }
        //Format 2nd list
        for(Appointment a : secondDoseAppointments) {
            AdminAppointment result = new AdminAppointment();
            result.setId(a.getAppointment_id());
            result.setUserId(a.getUser_id());
            result.setVaccinationCenter(a.getDose_2_center().name());
            result.setDate(a.getDose_2_date());
            result.setDoseNumber(2);
            result.setBrand(a.getDose_2_brand().name());
            results.add(result);
        }
        return results;
    }

    public boolean existsByUserId(Long userId) {
        return appointmentRepository.findByUserId(userId) != null;
    }

}
