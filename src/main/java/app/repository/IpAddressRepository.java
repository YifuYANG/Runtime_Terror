package app.repository;

import app.model.Ip_logs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IpAddressRepository extends JpaRepository<Ip_logs, Long> {
    @Query("select I from Ip_logs I where I.ip = :ip")
    Ip_logs findByIpAddress(@Param("ip") String ip);
}
