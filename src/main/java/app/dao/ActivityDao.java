package app.dao;

import app.model.Appointment;
import app.repository.AppointmentRepository;
import app.vo.ActivityForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityDao {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<Appointment> findAllActivities() {
        return appointmentRepository.findAll();
    }

    public List<ActivityForm> findAllActivitiesByUserId(Long id) {
        List<Appointment> appointments=appointmentRepository.findAllByUserId(id);
        List<ActivityForm> results = new ArrayList<>();
        for(Appointment a : appointments) {
            ActivityForm activity = new ActivityForm();

            if(a.getDose_1_center() != null) {
                activity.setFirst_dose_center(a.getDose_1_center().name());
            } else {
                activity.setFirst_dose_center("NA");
            }

            if(a.getDose_1_status() != null) {
                activity.setFirst_dose_status(a.getDose_1_status().name());
            } else {
                activity.setFirst_dose_status("NA");
            }

            if(a.getDose_1_date() != null) {
                activity.setFirst_dose_date(a.getDose_1_date().toString());
            } else {
                activity.setFirst_dose_date("NA");
            }

            if(a.getDose_1_Slot() != null) {
                activity.setFirst_dose_time(a.getDose_1_Slot().toString());
            } else {
                activity.setFirst_dose_time("NA");
            }

            if(a.getDose_2_center() != null) {
                activity.setSecond_dose_center(a.getDose_2_center().name());
            } else {
                activity.setSecond_dose_center("NA");
            }

            if(a.getDose_2_status() != null) {
                activity.setSecond_dose_status(a.getDose_2_status().name());
            } else {
                activity.setSecond_dose_status("NA");
            }

            if(a.getDose_2_date() != null) {
                activity.setSecond_dose_date(a.getDose_2_date().toString());
            } else {
                activity.setSecond_dose_date("NA");
            }

            if(a.getDose_2_slot() != null) {
                activity.setSecond_dose_time(a.getDose_2_slot().toString());
            } else {
                activity.setSecond_dose_time("NA");
            }
            results.add(activity);
        }

        return results;
    }

}
