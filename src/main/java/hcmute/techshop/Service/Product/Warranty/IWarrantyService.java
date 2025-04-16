package hcmute.techshop.Service.Product.Warranty;

import hcmute.techshop.Enum.WarrantyStatus;
import hcmute.techshop.Model.Product.CreateWarrantyRequest;
import hcmute.techshop.Model.Product.UpdateWarrantyRequest;
import hcmute.techshop.Model.Product.WarrantyModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IWarrantyService {
    // Tạo bảo hành
    WarrantyModel createWarranty(CreateWarrantyRequest request);
    // Lấy bảo hành theo id dùng chung cho admin và user (admin xem tất cả, user xem của mình bảo hành isActive = true)
    WarrantyModel getWarrantyById(Integer id, boolean isAdmin);
    // Lấy bảo hành theo mã dùng chung cho admin và user (admin xem tất cả, user xem của mình bảo hành isActive = true)
    WarrantyModel getWarrantyByCode(String wcode, boolean isAdmin);
    // Lấy tất cả bảo hành (admin) lấy tất cả bảo hành có tùy chọn lọc isActive = false
    Page<WarrantyModel> getAllWarranties(Pageable pageable, boolean includeDeleted);
    // Lấy bảo hành theo user
    List<WarrantyModel> getWarrantiesByUser(Integer userId);
    // Lấy bảo hành theo user email (cho người dùng xem bảo hành của họ)
    List<WarrantyModel> getWarrantiesByUserEmail(String email);
    // Lấy bảo hành theo product
    List<WarrantyModel> getWarrantiesByProduct(Integer productId);
    // Lấy bảo hành theo status
    Page<WarrantyModel> getWarrantiesByStatus(WarrantyStatus status, Pageable pageable);
    // Cập nhật status bảo hành
    WarrantyModel updateWarrantyStatus(Integer id, WarrantyStatus status);
    // Cập nhật bảo hành
    WarrantyModel updateWarranty(Integer id, UpdateWarrantyRequest request);
    // Xóa bảo hành
    boolean deleteWarranty(Integer id);
    //Khôi phục bảo hành đã xóa
    boolean restoreWarranty(Integer id);
    // Tìm kiếm bảo hành
    Page<WarrantyModel> searchWarranties(String keyword, Pageable pageable);
    // Lấy danh sách bảo hành hết hạn
    Page<WarrantyModel> getExpiredWarranties(Pageable pageable);
    // Lấy danh sách bảo hành sắp hết hạn (trong vòng 30 ngày)
    Page<WarrantyModel> getWarrantiesAboutToExpire(Pageable pageable);
    // Hủy bảo hành
    WarrantyModel cancelWarranty(Integer id, String reason);
}