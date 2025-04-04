package hcmute.techshop.Controller.Tracking;


import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Service.Tracking.TrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/tracking")
@RequiredArgsConstructor
public class TrackingAdminController {
    private final TrackingService trackingService;

    @GetMapping("/get_all")
    public ResponseEntity<ResponseModel> getAllTrakinng() {
        try {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Get all products attributes successfully")
                            .body(trackingService.getAllTrackings())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        }
    }
    @GetMapping("/get_detail/{id}")
    public ResponseEntity<ResponseModel> getDetailTrakinng(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Get tracking attributes successfully")
                            .body(trackingService.getDetailTracking(id))
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @DeleteMapping("/delete_tracking/{id}")
    public ResponseEntity<ResponseModel> deleteTrakinng(@PathVariable Long id) {
        try {
            trackingService.deleteTracking(id);
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Deleting model successfully")
                            .body(null)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        }
    }
}
