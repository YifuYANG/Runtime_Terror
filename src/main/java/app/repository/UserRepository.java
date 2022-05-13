package app.repository;

import app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.email = :email")
    User findByUserEmail(@Param("email") String userEmail);

    @Query("select u from User u where u.userId = :userId")
    User findByUserId(@Param("userId") Long userId);

    @Query("select u from User u where u.pps_number = :pps_number")
    User findByPPS(@Param("pps_number") String pps_number);

    @Query("select pps_number from User")
    List<String> findAllPPS();
}
