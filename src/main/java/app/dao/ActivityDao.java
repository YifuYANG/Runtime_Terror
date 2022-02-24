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
            if(a.getDose_1_center()!=null){
            activity.setFirst_dose_center(a.getDose_1_center().name());
            }
            activity.setFirst_dose_date(a.getDose_1_date());
            if(a.getDose_1_status()!=null){
            activity.setFirst_dose_status(a.getDose_1_status().name());
            }
            if(a.getDose_2_center()!=null){
            activity.setSecond_dose_center(a.getDose_2_center().name());
            }
            activity.setSecond_dose_date(a.getDose_2_date());
            if(a.getDose_2_status()!=null){
            activity.setSecond_dose_status(a.getDose_2_status().name());
            }
            results.add(activity);
        }

        return results;
    }

}
