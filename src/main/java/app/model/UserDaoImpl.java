package app.model;

import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

public class UserDaoImpl implements UserDao {
    private JdbcTemplate jdbcTemplate;

    public UserDaoImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    @Override
    public int registerUser(User user) {
        String sql = "INSERT INTO USER_DATA VALUES(?,?)";

        try {
            int counter = jdbcTemplate.update(sql, new Object[] { user.getUserId(), user.getPassword() });

            return counter;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public String loginUser(User user) {
        return null;
    }
}
