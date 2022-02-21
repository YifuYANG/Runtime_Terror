package app.repository;

import app.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("select a from Appointment a where a.user_id = :userId")
    Appointment findByUserId(@Param("userId") Long userId);

    @Query("select a from Appointment a where a.dose_1_status = 0")
    List<Appointment> findPendingFirstDose();

    @Query("select a from Appointment a where a.dose_2_status = 0")
    List<Appointment> findPendingSecondDose();

}
