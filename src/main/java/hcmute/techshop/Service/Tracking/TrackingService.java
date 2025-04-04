package hcmute.techshop.Service.Tracking;

import hcmute.techshop.Entity.Auth.UserTracking;
import hcmute.techshop.Repository.Auth.UserTrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackingService implements ITrackingService{
    private final UserTrackingRepository userTrackingRepository;
    @Override
    public List<UserTracking> getAllTrackings() {
        return userTrackingRepository.findAll().stream().toList();
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
}
