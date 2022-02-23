package app.dao;

import app.constant.DoseStatus;
import app.model.Appointment;
import app.vo.AdminAppointment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.List;


public class AppointmentDaoTest extends DaoTestBase {

    @Autowired
    private AppointmentDao appointmentDao;

    @Test
    public void testExistsByUserId() {
        Assertions.assertTrue(appointmentDao.existsByUserId(1L));
        Assertions.assertFalse(appointmentDao.existsByUserId(100L));
    }

    @Test
    public void testFindAll() {
        int actualSize = appointmentDao.findAll().size();
        Assertions.assertNotEquals(0, actualSize);
    }

    @Test
    public void testFindAllPendingAppointments() {
        List<AdminAppointment> result = appointmentDao.findAllPendingAppointments();
        Assertions.assertEquals(2, result.size());
    }

    @Transactional
    @Rollback(true)
    @Test
    public void testApproveDose1() {
        appointmentDao.updateDose1(1L);
        Appointment appointment = appointmentDao.findById(1L);
        Assertions.assertEquals(DoseStatus.RECEIVED, appointment.getDose_1_status());
    }

    @Transactional
    @Rollback(true)
    @Test
    public void testApproveDose2() {
        appointmentDao.updateDose1(2L);
        Appointment appointment = appointmentDao.findById(2L);
        Assertions.assertEquals(DoseStatus.RECEIVED, appointment.getDose_1_status());
    }

}
