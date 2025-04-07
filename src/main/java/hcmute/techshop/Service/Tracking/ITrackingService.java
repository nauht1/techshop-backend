package hcmute.techshop.Service.Tracking;

import hcmute.techshop.Entity.Auth.UserTracking;

import java.util.List;

public interface ITrackingService {
    List<UserTracking> getAllTrackings();
    UserTracking getDetailTracking(Long id);
    void deleteTracking(Long id);
}
