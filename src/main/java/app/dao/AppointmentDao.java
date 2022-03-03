package app.dao;

import app.constant.DoseBrand;
import app.constant.DoseSlot;
import app.model.Appointment;
import app.repository.AppointmentRepository;
import app.vo.AdminAppointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentDao {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public Appointment findById(Long id) {
        return appointmentRepository.findById(id).get();
    }

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public Appointment findByUserId(Long id) {
        return appointmentRepository.findByUserId(id);
    }

    public int getNumberOfPfizer() {
        return appointmentRepository.countDose1Brand(DoseBrand.PFIZER) +
                appointmentRepository.countDose2Brand(DoseBrand.PFIZER);
    }

    public int getNumberOfModerna() {
        return appointmentRepository.countDose1Brand(DoseBrand.MODERNA) +
                appointmentRepository.countDose2Brand(DoseBrand.MODERNA);
    }

    public List<Appointment> findAllByDate1(Date date){ return appointmentRepository.findAllByDate1(date);}

    public List<Appointment> findAllByDate2(Date date){ return appointmentRepository.findAllByDate2(date);}

    public Appointment findAppointmentByDateAndSlot(Date date, DoseSlot slot) { return appointmentRepository.findAppointmentByDateAndSlot(date, slot); }

    public Appointment findSecondAppointmentByDateAndSlot(Date date, DoseSlot slot) { return appointmentRepository.findSecondAppointmentByDateAndSlot(date, slot); }

    @Transactional
    public void updateDose1(Long id) {
        appointmentRepository.updateDose1(id);
    }

    @Transactional
    public void updateDose2(Long id) {
        appointmentRepository.updateDose2(id);
    }

    public boolean existsById(Long id) {
        return appointmentRepository.existsById(id);
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
            result.setStatus(a.getDose_1_status().name());
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
            result.setStatus(a.getDose_2_status().name());
            results.add(result);
        }
        return results;
    }

    public boolean existsByUserId(Long userId) {
        return appointmentRepository.findByUserId(userId) != null;
    }

}
