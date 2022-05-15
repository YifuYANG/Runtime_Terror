package app.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

public class ForumPostDaoTest {

    @Test
    public void testTimeDiff() {
        Timestamp time1 = new Timestamp(System.currentTimeMillis());
        Timestamp time2 = new Timestamp(time1.getTime() + 3000);
        int diff = (int) (time2.getTime() - time1.getTime()) / 1000;
        Assertions.assertEquals(3, diff);
    }

}
