package hcmute.techshop.Controller.Tracking;


import hcmute.techshop.Entity.Auth.UserTracking;
import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Service.Tracking.TrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/tracking")
@RequiredArgsConstructor
public class TrackingAdminController {
    private final TrackingService trackingService;

    @GetMapping("/get_all")
    public ResponseEntity<ResponseModel> getAllTracking(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "timestamp,desc") String sort,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            String[] sortParams = sort.split(",");
            Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);
            Sort sortBy = Sort.by(direction, sortParams[0]);
            PageRequest pageRequest = PageRequest.of(page, size, sortBy);

            Page<UserTracking> trackingPage = trackingService.getAllTrackings(pageRequest, userId, productId, startDate, endDate);


            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Get all tracking records successfully")
                            .body(Map.of(
                                    "content", trackingPage.getContent(),
                                    "totalElements", trackingPage.getTotalElements(),
                                    "totalPages", trackingPage.getTotalPages(),
                                    "currentPage", trackingPage.getNumber()
                            ))
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(false)
                            .message("Failed to get tracking records: " + e.getMessage())
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
    public ResponseEntity<ResponseModel> deleteTracking(@PathVariable Long id) {
        try {
            trackingService.deleteTracking(id);
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Tracking record deleted successfully")
                            .body(null)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(false)
                            .message("Failed to delete tracking record: " + e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping("/summary/by_product")
    public ResponseEntity<ResponseModel> getProductActivitySummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            Map<Integer, Map<String, Long>> summary = trackingService.getProductActivitySummary(startDate, endDate);
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Get product activity summary successfully")
                            .body(summary)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(false)
                            .message("Failed to get product activity summary: " + e.getMessage())
                            .build()
            );
        }
    }
    @GetMapping("/summary/by_user")
    public ResponseEntity<ResponseModel> getUserActivitySummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            Map<Integer, Map<String, Long>> summary = trackingService.getUserActivitySummary(startDate, endDate);
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Get user activity summary successfully")
                            .body(summary)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(false)
                            .message("Failed to get user activity summary: " + e.getMessage())
                            .build()
            );
        }
    }
    @DeleteMapping("/delete_old")
    public ResponseEntity<ResponseModel> deleteOldTracking(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime beforeDate) {
        try {
            long deletedCount = trackingService.deleteOldTracking(beforeDate);
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Deleted " + deletedCount + " old tracking records successfully")
                            .body(null)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(false)
                            .message("Failed to delete old tracking records: " + e.getMessage())
                            .build()
            );
        }
    }
}
