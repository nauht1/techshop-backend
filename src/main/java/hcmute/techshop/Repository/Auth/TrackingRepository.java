package hcmute.techshop.Repository.Auth;

import hcmute.techshop.Entity.Auth.TrackingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackingRepository extends JpaRepository<TrackingEntity, Integer> {
}
