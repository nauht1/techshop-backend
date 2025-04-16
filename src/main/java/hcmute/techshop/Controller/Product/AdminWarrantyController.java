package hcmute.techshop.Controller.Product;

import hcmute.techshop.Enum.WarrantyStatus;
import hcmute.techshop.Exception.BadRequestException;
import hcmute.techshop.Model.Product.CreateWarrantyRequest;
import hcmute.techshop.Model.Product.UpdateWarrantyRequest;
import hcmute.techshop.Model.Product.WarrantyModel;
import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Service.Product.Warranty.IWarrantyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/warranties")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")  // Chỉ admin mới có quyền truy cập APIs này
public class AdminWarrantyController {

    private final IWarrantyService warrantyService;
    
    // Admin tạo bảo hành mới
    @PostMapping
    public ResponseEntity<ResponseModel> createWarranty(@RequestBody CreateWarrantyRequest request) {
        if (request == null) {
            throw new BadRequestException("Thông tin tạo bảo hành không được để trống");
        }
        
        if (request.getUserId() == null) {
            throw new BadRequestException("ID người dùng không được để trống");
        }
        
        if (request.getUserId() <= 0) {
            throw new BadRequestException("ID người dùng phải là số dương");
        }
        
        if (request.getProductId() == null) {
            throw new BadRequestException("ID sản phẩm không được để trống");
        }
        
        if (request.getProductId() <= 0) {
            throw new BadRequestException("ID sản phẩm phải là số dương");
        }
        
        // Kiểm tra status có thuộc enum WarrantyStatus
        if (request.getStatus() != null) {
            boolean isValidStatus = false;
            for (WarrantyStatus status : WarrantyStatus.values()) {
                if (status == request.getStatus()) {
                    isValidStatus = true;
                    break;
                }
            }
            if (!isValidStatus) {
                throw new BadRequestException("Trạng thái bảo hành không hợp lệ. Các trạng thái hợp lệ: " + 
                    Arrays.toString(WarrantyStatus.values()));
            }
        }
        
        if (request.getEndDate() != null && request.getEndDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Ngày hết hạn không hợp lệ");
        }
        
        WarrantyModel warranty = warrantyService.createWarranty(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ResponseModel(true, "Tạo bảo hành thành công", warranty));
    }
    
    // Admin lấy thông tin một bảo hành
    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel> getWarrantyById(@PathVariable Integer id) {
        if (id == null) {
            throw new BadRequestException("ID bảo hành không được để trống");
        }
        
        if (id <= 0) {
            throw new BadRequestException("ID bảo hành phải là số dương");
        }
        
        WarrantyModel warranty = warrantyService.getWarrantyById(id, true);
        return ResponseEntity.ok(new ResponseModel(true, "Lấy thông tin bảo hành thành công", warranty));
    }
    
    // Admin lấy thông tin bảo hành theo mã
    @GetMapping("/code/{wcode}")
    public ResponseEntity<ResponseModel> getWarrantyByCode(@PathVariable String wcode) {
        if (wcode == null || wcode.trim().isEmpty()) {
            throw new BadRequestException("Mã bảo hành không được để trống");
        }
        
        if (wcode.trim().length() < 3) {
            throw new BadRequestException("Mã bảo hành phải có ít nhất 3 ký tự");
        }
        
        WarrantyModel warranty = warrantyService.getWarrantyByCode(wcode, true);
        return ResponseEntity.ok(new ResponseModel(true, "Lấy thông tin bảo hành thành công", warranty));
    }
    
    // Admin lấy tất cả bảo hành với phân trang và sắp xếp mặc định là id tăng dần và không bao gồm bảo hành đã xóa
    @GetMapping
    public ResponseEntity<ResponseModel> getAllWarranties(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "false") boolean includeDeleted) {
        
        if (page < 0) {
            throw new BadRequestException("Số trang phải lớn hơn hoặc bằng 0");
        }
        
        List<String> validSortFields = Arrays.asList("id", "startDate", "endDate", "updatedAt", "status");
        if (!validSortFields.contains(sortBy)) {
            throw new BadRequestException("Sắp xếp không hợp lệ. Các trường hợp lệ: " + validSortFields);
        }
        
        if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
            throw new BadRequestException("Sắp xếp là 'asc' hoặc 'desc'");
        }
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? 
            Sort.Direction.ASC : Sort.Direction.DESC;
            
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<WarrantyModel> warranties = warrantyService.getAllWarranties(pageable, includeDeleted);
        
        return ResponseEntity.ok(new ResponseModel(true, "Lấy danh sách bảo hành thành công", warranties));
    }
    
    // Admin lấy danh sách bảo hành theo người dùng
    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseModel> getWarrantiesByUser(@PathVariable Integer userId) {
        if (userId == null) {
            throw new BadRequestException("ID người dùng không được để trống");
        }
        
        if (userId <= 0) {
            throw new BadRequestException("ID người dùng phải là số dương");
        }
        
        List<WarrantyModel> warranties = warrantyService.getWarrantiesByUser(userId);
        return ResponseEntity.ok(new ResponseModel(true, "Lấy danh sách bảo hành theo người dùng thành công", warranties));
    }
    
    // Admin lấy danh sách bảo hành theo sản phẩm
    @GetMapping("/product/{productId}")
    public ResponseEntity<ResponseModel> getWarrantiesByProduct(@PathVariable Integer productId) {
        if (productId == null) {
            throw new BadRequestException("ID sản phẩm không được để trống");
        }
        
        if (productId <= 0) {
            throw new BadRequestException("ID sản phẩm phải là số dương");
        }
        
        List<WarrantyModel> warranties = warrantyService.getWarrantiesByProduct(productId);
        return ResponseEntity.ok(new ResponseModel(true, "Lấy danh sách bảo hành theo sản phẩm thành công", warranties));
    }
    
    // Admin lấy danh sách bảo hành theo trạng thái
    @GetMapping("/status/{status}")
    public ResponseEntity<ResponseModel> getWarrantiesByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        if (status == null || status.trim().isEmpty()) {
            throw new BadRequestException("Trạng thái bảo hành không được để trống");
        }
        
        // Kiểm tra status có thuộc enum WarrantyStatus
        WarrantyStatus warrantyStatus;
        try {
            warrantyStatus = WarrantyStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Trạng thái bảo hành không hợp lệ. Các trạng thái hợp lệ: " + 
                Arrays.toString(WarrantyStatus.values()));
        }
        
        if (page < 0) {
            throw new BadRequestException("Số trang phải lớn hơn hoặc bằng 0");
        }
        
        Pageable pageable = PageRequest.of(page, size);
        Page<WarrantyModel> warranties = warrantyService.getWarrantiesByStatus(warrantyStatus, pageable);
        
        return ResponseEntity.ok(new ResponseModel(true, "Lấy danh sách bảo hành theo trạng thái thành công", warranties));
    }
    
    // Admin cập nhật trạng thái bảo hành
    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseModel> updateWarrantyStatus(
            @PathVariable Integer id,
            @RequestParam String status) {
        
        if (id == null) {
            throw new BadRequestException("ID bảo hành không được để trống");
        }
        
        if (id <= 0) {
            throw new BadRequestException("ID bảo hành phải là số dương");
        }
        
        if (status == null || status.trim().isEmpty()) {
            throw new BadRequestException("Trạng thái bảo hành không được để trống");
        }
        
        // Kiểm tra status có thuộc enum WarrantyStatus không
        WarrantyStatus warrantyStatus;
        try {
            warrantyStatus = WarrantyStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Trạng thái bảo hành không hợp lệ. Các trạng thái hợp lệ: " + 
                Arrays.toString(WarrantyStatus.values()));
        }
        
        WarrantyModel warranty = warrantyService.updateWarrantyStatus(id, warrantyStatus);
        return ResponseEntity.ok(new ResponseModel(true, "Cập nhật trạng thái bảo hành thành công", warranty));
    }
    
    // API riêng cho việc hủy bảo hành
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ResponseModel> cancelWarranty(
            @PathVariable Integer id,
            @RequestParam String reason) {
        
        if (id == null) {
            throw new BadRequestException("ID bảo hành không được để trống");
        }
        
        if (id <= 0) {
            throw new BadRequestException("ID bảo hành phải là số dương");
        }
        
        if (reason == null || reason.trim().isEmpty()) {
            throw new BadRequestException("Lý do hủy bảo hành không được để trống");
        }
        
        if (reason.trim().length() < 5) {
            throw new BadRequestException("Lý do hủy bảo hành phải có ít nhất 5 ký tự");
        }
        
        WarrantyModel warranty = warrantyService.cancelWarranty(id, reason);
        return ResponseEntity.ok(new ResponseModel(true, "Hủy bảo hành thành công", warranty));
    }
    
    // Admin cập nhật thông tin bảo hành
    @PutMapping("/{id}")
    public ResponseEntity<ResponseModel> updateWarranty(
            @PathVariable Integer id,
            @RequestBody UpdateWarrantyRequest request) {
        
        if (id == null) {
            throw new BadRequestException("ID bảo hành không được để trống");
        }
        
        if (id <= 0) {
            throw new BadRequestException("ID bảo hành phải là số dương");
        }
        
        if (request == null) {
            throw new BadRequestException("Thông tin cập nhật không được để trống");
        }
        
        // Kiểm tra trạng thái nếu được cung cấp có thuộc enum WarrantyStatus không
        if (request.getStatus() != null) {
            boolean isValidStatus = false;
            for (WarrantyStatus status : WarrantyStatus.values()) {
                if (status == request.getStatus()) {
                    isValidStatus = true;
                    break;
                }
            }
            
            if (!isValidStatus) {
                throw new BadRequestException("Trạng thái bảo hành không hợp lệ. Các trạng thái hợp lệ: " + 
                    Arrays.toString(WarrantyStatus.values()));
            }
        }
        
        // Kiểm tra ngày hết hạn nếu được cung cấp
        if (request.getEndDate() != null && request.getEndDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Ngày hết hạn không hợp lệ");
        }
        
        // Kiểm tra lý do hủy nếu trạng thái là CANCELLED
        if (request.getStatus() == WarrantyStatus.CANCELLED) {
            if (request.getCancellationReason() == null || request.getCancellationReason().trim().isEmpty()) {
                throw new BadRequestException("Lý do hủy bảo hành không được để trống khi chuyển sang trạng thái HỦY");
            }
        }
        
        WarrantyModel warranty = warrantyService.updateWarranty(id, request);
        return ResponseEntity.ok(new ResponseModel(true, "Cập nhật thông tin bảo hành thành công", warranty));
    }
    
    // Admin xóa bảo hành
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseModel> deleteWarranty(@PathVariable Integer id) {
        if (id == null) {
            throw new BadRequestException("ID bảo hành không được để trống");
        }
        
        if (id <= 0) {
            throw new BadRequestException("ID bảo hành phải là số dương");
        }
        
        boolean result = warrantyService.deleteWarranty(id);
        return ResponseEntity.ok(new ResponseModel(true, "Xóa bảo hành thành công", result));
    }

    //Khôi phục bảo hành đã xóa
    @PatchMapping("/{id}/restore")
    public ResponseEntity<ResponseModel> restoreWarranty(@PathVariable Integer id) {
        if (id == null) {
            throw new BadRequestException("ID bảo hành không được để trống");
        }
        
        if (id <= 0) {
            throw new BadRequestException("ID bảo hành phải là số dương");
        }
        
        boolean result = warrantyService.restoreWarranty(id);
        return ResponseEntity.ok(new ResponseModel(true, "Khôi phục bảo hành thành công", result));
    }
    
    // Admin tìm kiếm bảo hành
    @GetMapping("/search")
    public ResponseEntity<ResponseModel> searchWarranties(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BadRequestException("Từ khóa tìm kiếm không được để trống");
        }
        
        if (page < 0) {
            throw new BadRequestException("Số trang phải lớn hơn hoặc bằng 0");
        }
        
        Pageable pageable = PageRequest.of(page, size);
        Page<WarrantyModel> warranties = warrantyService.searchWarranties(keyword, pageable);
        
        return ResponseEntity.ok(new ResponseModel(true, "Tìm kiếm bảo hành thành công", warranties));
    }
    
    // Admin lấy danh sách bảo hành đã hết hạn
    @GetMapping("/expired")
    public ResponseEntity<ResponseModel> getExpiredWarranties(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        if (page < 0) {
            throw new BadRequestException("Số trang phải lớn hơn hoặc bằng 0");
        }
        
        Pageable pageable = PageRequest.of(page, size);
        Page<WarrantyModel> warranties = warrantyService.getExpiredWarranties(pageable);
        
        return ResponseEntity.ok(new ResponseModel(true, "Lấy danh sách bảo hành hết hạn thành công", warranties));
    }
    
    // Admin lấy danh sách bảo hành sắp hết hạn mặc định là endDate tăng dần
    @GetMapping("/about-to-expire")
    public ResponseEntity<ResponseModel> getWarrantiesAboutToExpire(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "endDate") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        
        if (page < 0) {
            throw new BadRequestException("Số trang phải lớn hơn hoặc bằng 0");
        }
        
        List<String> validSortFields = Arrays.asList("id", "startDate", "endDate", "updatedAt");
        if (!validSortFields.contains(sortBy)) {
            throw new BadRequestException("Sắp xếp không hợp lệ. Các trường hợp lệ: " + validSortFields);
        }
        
        if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
            throw new BadRequestException("Sắp xếp là 'asc' hoặc 'desc'");
        }
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? 
            Sort.Direction.ASC : Sort.Direction.DESC;
            
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<WarrantyModel> warranties = warrantyService.getWarrantiesAboutToExpire(pageable);
        
        return ResponseEntity.ok(new ResponseModel(true, "Lấy danh sách bảo hành sắp hết hạn thành công", warranties));
    }
}