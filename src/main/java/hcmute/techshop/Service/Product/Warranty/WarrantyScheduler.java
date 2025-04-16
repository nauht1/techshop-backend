package hcmute.techshop.Service.Product.Warranty;

import hcmute.techshop.Entity.Product.WarrantyEntity;
import hcmute.techshop.Enum.WarrantyStatus;
import hcmute.techshop.Repository.Product.WarrantyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class WarrantyScheduler {

    private final WarrantyRepository warrantyRepository;
    
    // Chạy hàng ngày lúc 1 giờ sáng
    // @Scheduled(cron = "0 0 1 * * ?")

    // Chạy mỗi 10phút
    @Scheduled(cron = "0 */10 * * * ?")
    public void updateExpiredWarranties() {
        LocalDateTime now = LocalDateTime.now();
        log.info("Đang chạy job cập nhật bảo hành hết hạn: {}", now);
        
        try {
            // Tìm tất cả bảo hành ACTIVE đã hết hạn
            List<WarrantyEntity> expiredWarranties = warrantyRepository.findByStatusAndEndDateBeforeAndIsActiveTrue(
                WarrantyStatus.ACTIVE, now);
            
            log.info("Tìm thấy {} bảo hành đã hết hạn", expiredWarranties.size());
            
            // Cập nhật trạng thái
            for (WarrantyEntity warranty : expiredWarranties) {
                warranty.setStatus(WarrantyStatus.EXPIRED);
                warranty.setUpdatedAt(now);
                warrantyRepository.save(warranty);
                log.debug("Đã cập nhật bảo hành ID {} sang trạng thái EXPIRED", warranty.getId());
            }
            
            log.info("Đã cập nhật thành công {} bảo hành sang trạng thái EXPIRED", expiredWarranties.size());
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật bảo hành hết hạn: {}", e.getMessage(), e);
        }
    }
}