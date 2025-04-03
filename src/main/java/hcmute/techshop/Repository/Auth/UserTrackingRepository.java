package hcmute.techshop.Repository.Auth;

import hcmute.techshop.Entity.Auth.UserTracking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTrackingRepository extends JpaRepository<UserTracking, Long> {
}
