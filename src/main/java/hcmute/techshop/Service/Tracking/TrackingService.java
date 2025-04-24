package hcmute.techshop.Service.Tracking;

import hcmute.techshop.Entity.Auth.TrackingEntity;
import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Auth.UserTracking;
import hcmute.techshop.Enum.EventType;
import hcmute.techshop.Repository.Auth.TrackingRepository;
import hcmute.techshop.Repository.Auth.UserTrackingRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrackingService implements ITrackingService{
    private final UserTrackingRepository userTrackingRepository;
    private final TrackingRepository trackingRepository;

    public Page<UserTracking> getAllTrackings(PageRequest pageRequest, Long userId, Long productId, LocalDateTime startDate, LocalDateTime endDate) {
        Specification<UserTracking> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userId != null) {
                predicates.add(cb.equal(root.get("user").get("id"), userId));
            }
            if (productId != null) {
                predicates.add(cb.equal(root.get("product").get("id"), productId));
            }
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("timestamp"), startDate.toString()));
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("timestamp"), endDate.toString()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return userTrackingRepository.findAll(spec, pageRequest);
    }

    @Override
    public UserTracking getDetailTracking(Long id) {
        return userTrackingRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Cannot find the 'Tracking' in system!")
        );
    }

    @Override
    public void deleteTracking(Long id) {
        userTrackingRepository.deleteById(id);
    }

    @Override
    public void track(UserEntity user, EventType eventType, String eventData) {
        TrackingEntity tracking = new TrackingEntity();
        tracking.setUser(user);
        tracking.setEventType(eventType);
        tracking.setEventData(eventData);
        tracking.setUpdatedAt(LocalDateTime.now());

        trackingRepository.save(tracking);
    }


    public long deleteOldTracking(LocalDateTime beforeDate) {
        return userTrackingRepository.deleteByTimestampBefore(beforeDate);
    }

    public Map<Integer, Map<String, Long>> getProductActivitySummary(LocalDateTime startDate, LocalDateTime endDate) {
        Specification<UserTracking> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isNotNull(root.get("product")));
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("timestamp"), startDate));
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("timestamp"), endDate));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        List<UserTracking> trackings = userTrackingRepository.findAll(spec);
        return trackings.stream()
                .collect(Collectors.groupingBy(
                        tracking -> tracking.getProduct().getId(),
                        Collectors.groupingBy(
                                UserTracking::getAction,
                                Collectors.counting()
                        )
                ));
    }

    public Map<Integer, Map<String, Long>> getUserActivitySummary(LocalDateTime startDate, LocalDateTime endDate) {
        Specification<UserTracking> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("timestamp"), startDate));
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("timestamp"), endDate));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        List<UserTracking> trackings = userTrackingRepository.findAll(spec);
        return trackings.stream()
                .collect(Collectors.groupingBy(
                        tracking -> tracking.getUser().getId(),
                        Collectors.groupingBy(
                                UserTracking::getAction,
                                Collectors.counting()
                        )
                ));
    }

}
