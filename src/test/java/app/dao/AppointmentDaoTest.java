package app.dao;

import app.constant.DoseBrand;
import app.constant.DoseStatus;
import app.constant.VaccinationCenter;
import app.model.Appointment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.sql.Date;

public class AppointmentDaoTest extends DaoTestBase {

    @Autowired
    private AppointmentDao appointmentDao;

    @Transactional
    @Rollback(value = true)
    @Test
    public void testSave() {
        Appointment appointment = new Appointment(
                1L,
                1L,
                VaccinationCenter.UCD,
                null,
                DoseStatus.PENDING,
                null,
                Date.valueOf("2022-03-01"),
                null,
                DoseBrand.PFIZER,
                null
        );
    }

    @Test
    public void testFindAll() {
        int actualSize = appointmentDao.findAll().size();
        Assertions.assertNotEquals(0, actualSize);
    }

}
