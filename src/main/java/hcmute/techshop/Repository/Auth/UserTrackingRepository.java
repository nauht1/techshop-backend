package hcmute.techshop.Repository.Auth;

import hcmute.techshop.Entity.Auth.UserTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface UserTrackingRepository extends JpaRepository<UserTracking, Long>, JpaSpecificationExecutor<UserTracking> {

    @Modifying
    @Query("DELETE FROM UserTracking ut WHERE ut.timestamp < :beforeDate")
    long deleteByTimestampBefore(LocalDateTime beforeDate);
}