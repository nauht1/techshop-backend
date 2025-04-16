package hcmute.techshop.Controller.Product;

import hcmute.techshop.Exception.BadRequestException;
import hcmute.techshop.Model.Product.WarrantyModel;
import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Service.Product.Warranty.IWarrantyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/warranties")
@RequiredArgsConstructor
public class UserWarrantyController {

    private final IWarrantyService warrantyService;

    // Người dùng xem các bảo hành của mình
    @GetMapping("/my-warranties")
    public ResponseEntity<ResponseModel> getMyWarranties() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null) {
            throw new BadRequestException("Không tìm thấy thông tin xác thực");
        }
        
        if (authentication.getName() == null || authentication.getName().trim().isEmpty()) {
            throw new BadRequestException("Thông tin người dùng không hợp lệ");
        }
        
        String currentUserEmail = authentication.getName();
        // Lấy danh sách bảo hành của người dùng hiện tại theo email và chỉ xem được bảo hành isActive = true
        List<WarrantyModel> warranties = warrantyService.getWarrantiesByUserEmail(currentUserEmail);
        return ResponseEntity.ok(new ResponseModel(true, "Lấy danh sách bảo hành thành công", warranties));
    }
    
    // Người dùng xem chi tiết một bảo hành cụ thể ( thuộc về chính user)
    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel> getWarrantyById(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null) {
            throw new BadRequestException("Không tìm thấy thông tin xác thực");
        }
        
        if (authentication.getName() == null || authentication.getName().trim().isEmpty()) {
            throw new BadRequestException("Thông tin người dùng không hợp lệ");
        }
        
        if (id == null) {
            throw new BadRequestException("ID bảo hành không được để trống");
        }
        
        String currentUserEmail = authentication.getName();
        
        WarrantyModel warranty = warrantyService.getWarrantyById(id, false);
        
        // Kiểm tra xem bảo hành có thuộc về người dùng hiện tại không
        if (!warranty.getUserEmail().equals(currentUserEmail)) {
            return ResponseEntity.status(403)
                .body(new ResponseModel(false, "Bạn không có quyền xem thông tin bảo hành này", null));
        }
        
        return ResponseEntity.ok(new ResponseModel(true, "Lấy thông tin bảo hành thành công", warranty));
    }
    
    // Người dùng kiểm tra bảo hành theo mã
    @GetMapping("/check/{wcode}")
    public ResponseEntity<ResponseModel> checkWarrantyByCode(@PathVariable String wcode) {
        if (wcode == null || wcode.trim().isEmpty()) {
            throw new BadRequestException("Mã bảo hành không được để trống");
        }
        
        if (wcode.trim().length() < 3) {
            throw new BadRequestException("Mã bảo hành phải có ít nhất 3 ký tự");
        }
        
        try {
            WarrantyModel warranty = warrantyService.getWarrantyByCode(wcode, false);
            return ResponseEntity.ok(new ResponseModel(true, "Kiểm tra bảo hành thành công", warranty));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseModel(false, "Không tìm thấy thông tin bảo hành với mã " + wcode, null));
        }
    }
}