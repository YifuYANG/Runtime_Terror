package app.dao;

import app.vo.AdminAppointment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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

}
