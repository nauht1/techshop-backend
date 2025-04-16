package hcmute.techshop.Service.Product.Warranty;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Product.ProductEntity;
import hcmute.techshop.Entity.Product.WarrantyEntity;
import hcmute.techshop.Enum.WarrantyStatus;
import hcmute.techshop.Exception.BadRequestException;
import hcmute.techshop.Exception.ResourceNotFoundException;
import hcmute.techshop.Model.Product.CreateWarrantyRequest;
import hcmute.techshop.Model.Product.UpdateWarrantyRequest;
import hcmute.techshop.Model.Product.WarrantyModel;
import hcmute.techshop.Repository.Auth.UserRepository;
import hcmute.techshop.Repository.Product.ProductRepository;
import hcmute.techshop.Repository.Product.WarrantyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarrantyServiceImpl implements IWarrantyService {
    
    private final WarrantyRepository warrantyRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // Tạo bảo hành
    @Override
    public WarrantyModel createWarranty(CreateWarrantyRequest request) {
        UserEntity user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
            
        ProductEntity product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));
        
        LocalDateTime now = LocalDateTime.now();
        
        LocalDateTime endDate = request.getEndDate() != null ? 
            request.getEndDate() : now.plusMonths(12); // Mặc định 12 tháng
        
        WarrantyStatus status = request.getStatus() != null ? 
            request.getStatus() : WarrantyStatus.ACTIVE;
        
        WarrantyEntity warranty = new WarrantyEntity();
        warranty.setWcode("WR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        warranty.setUser(user);
        warranty.setProduct(product);
        warranty.setStatus(status);
        warranty.setStartDate(now);
        warranty.setEndDate(endDate);
        warranty.setUpdatedAt(now);
        warranty.setActive(true);
        
        WarrantyEntity savedWarranty = warrantyRepository.save(warranty);
        return convertToModel(savedWarranty);
    }

    // Lấy bảo hành theo id
    @Override
    public WarrantyModel getWarrantyById(Integer id, boolean isAdmin) {
        WarrantyEntity warranty;
        if (isAdmin) {
            // Admin xem được tất cả bảo hành
            warranty = warrantyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bảo hành với ID: " + id));
        } else {
            // User thường chỉ xem được bảo hành isActive = true
            warranty = warrantyRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bảo hành với ID: " + id));
        }
        
        return convertToModel(warranty);
    }

    // Lấy bảo hành theo mã
    @Override
    public WarrantyModel getWarrantyByCode(String wcode, boolean isAdmin) {
        WarrantyEntity warranty;
        if (isAdmin) {
            warranty = warrantyRepository.findByWcode(wcode)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bảo hành với mã " + wcode));
        } else {
            warranty = warrantyRepository.findByWcodeAndIsActiveTrue(wcode)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bảo hành với mã " + wcode));
        }
        return convertToModel(warranty);
    }

    // Lấy tất cả bảo hành
    @Override
    public Page<WarrantyModel> getAllWarranties(Pageable pageable, boolean includeDeleted) {
        if (includeDeleted==true) {
            return warrantyRepository.findAll(pageable)
                .map(this::convertToModel);
        } else {
            return warrantyRepository.findByIsActiveTrue(pageable)
                .map(this::convertToModel);
        }
    }

    // Lấy bảo hành theo user
    @Override
    public List<WarrantyModel> getWarrantiesByUser(Integer userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + userId));
            
        return warrantyRepository.findByUser(user).stream()
            .map(this::convertToModel)
            .collect(Collectors.toList());
    }
    
    // Lấy bảo hành theo user email
    @Override
    public List<WarrantyModel> getWarrantiesByUserEmail(String email) {
        return warrantyRepository.findByUserEmail(email).stream()
            .map(this::convertToModel)
            .collect(Collectors.toList());
    }

    // Lấy bảo hành theo product
    @Override
    public List<WarrantyModel> getWarrantiesByProduct(Integer productId) {
        ProductEntity product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + productId));
            
        return warrantyRepository.findByProduct(product).stream()
            .map(this::convertToModel)
            .collect(Collectors.toList());
    }

    // Lấy bảo hành theo status
    @Override
    public Page<WarrantyModel> getWarrantiesByStatus(WarrantyStatus status, Pageable pageable) {
        return warrantyRepository.findByStatusAndIsActiveTrue(status, pageable)
            .map(this::convertToModel);
    }

    // Cập nhật status bảo hành
    @Override
    public WarrantyModel updateWarrantyStatus(Integer id, WarrantyStatus status) {
        WarrantyEntity warranty = warrantyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bảo hành với ID: " + id));
        
        validateStatusTransition(warranty.getStatus(), status);
        
        warranty.setStatus(status);
        warranty.setUpdatedAt(LocalDateTime.now());
        
        WarrantyEntity updatedWarranty = warrantyRepository.save(warranty);
        return convertToModel(updatedWarranty);
    }

    // Cập nhật bảo hành
    @Override
    public WarrantyModel updateWarranty(Integer id, UpdateWarrantyRequest request) {
        WarrantyEntity warranty = warrantyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bảo hành với ID: " + id));
        
        if (request.getStatus() != null && request.getStatus() != warranty.getStatus()) {
            validateStatusTransition(warranty.getStatus(), request.getStatus());
            warranty.setStatus(request.getStatus());
            
            if (request.getStatus() == WarrantyStatus.CANCELLED && 
                (request.getCancellationReason() == null || request.getCancellationReason().trim().isEmpty())) {
                throw new BadRequestException("Lý do hủy bảo hành không được để trống");
            }
        }
        
        // Update các thông tin khác
        if (request.getEndDate() != null) {
            warranty.setEndDate(request.getEndDate());
        }
        
        if (request.getCancellationReason() != null && !request.getCancellationReason().trim().isEmpty()) {
            warranty.setCancellationReason(request.getCancellationReason());
        }
        
        warranty.setUpdatedAt(LocalDateTime.now());
        
        WarrantyEntity updatedWarranty = warrantyRepository.save(warranty);
        return convertToModel(updatedWarranty);
    }

    // Hủy bảo hành
    @Override
    public WarrantyModel cancelWarranty(Integer id, String reason) {
        WarrantyEntity warranty = warrantyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bảo hành với ID: " + id));
        
        if (warranty.getStatus() == WarrantyStatus.CANCELLED) {
            throw new BadRequestException("Bảo hành đã ở trạng thái hủy");
        }
        
        warranty.setStatus(WarrantyStatus.CANCELLED);
        warranty.setCancellationReason(reason);
        warranty.setUpdatedAt(LocalDateTime.now());
        
        WarrantyEntity cancelledWarranty = warrantyRepository.save(warranty);
        return convertToModel(cancelledWarranty);
    }
    
    // Xóa bảo hành
    @Override
    public boolean deleteWarranty(Integer id) {
        WarrantyEntity warranty = warrantyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bảo hành với ID: " + id));
        
        warranty.setActive(false);
        warranty.setUpdatedAt(LocalDateTime.now());
        warrantyRepository.save(warranty);
        return true;
    }

    //Khôi phục bảo hành đã xóa
    @Override
    public boolean restoreWarranty(Integer id) {
        WarrantyEntity warranty = warrantyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bảo hành với ID: " + id));
        warranty.setActive(true);
        warranty.setUpdatedAt(LocalDateTime.now());
        warrantyRepository.save(warranty);
        return true;
    }

    // Tìm kiếm bảo hành
    @Override
    public Page<WarrantyModel> searchWarranties(String keyword, Pageable pageable) {
        return warrantyRepository.searchWarranties(keyword, pageable)
            .map(this::convertToModel);
    }
    
    // Lấy danh sách bảo hành đã hết hạn
    @Override
    public Page<WarrantyModel> getExpiredWarranties(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        return warrantyRepository.findExpiredWarranties(now, pageable)
            .map(this::convertToModel);
    }
    
    // Lấy danh sách bảo hành sắp hết hạn
    @Override
    public Page<WarrantyModel> getWarrantiesAboutToExpire(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime future = now.plusDays(30); // Sắp hết hạn trong 30 ngày
        return warrantyRepository.findWarrantiesAboutToExpire(now, future, pageable)
            .map(this::convertToModel);
    }

    // Kiểm tra việc chuyển đổi trạng thái có hợp lệ không
    private void validateStatusTransition(WarrantyStatus currentStatus, WarrantyStatus newStatus) {
        // Từ CANCELLED không thể chuyển sang trạng thái khác
        if (currentStatus == WarrantyStatus.CANCELLED && newStatus != WarrantyStatus.CANCELLED) {
            throw new BadRequestException("Không thể thay đổi trạng thái của bảo hành đã hủy");
        }
        
        // Từ EXPIRED chỉ có thể chuyển sang ACTIVE (gia hạn) hoặc CANCELLED
        if (currentStatus == WarrantyStatus.EXPIRED && newStatus != WarrantyStatus.ACTIVE 
                && newStatus != WarrantyStatus.CANCELLED) {
            throw new BadRequestException("Từ trạng thái EXPIRED chỉ có thể chuyển sang trạng thái ACTIVE hoặc CANCELLED");
        }
        
        // Đảm bảo trạng thái thuộc enum WarrantyStatus
        try {
            WarrantyStatus.valueOf(newStatus.name());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Trạng thái bảo hành không hợp lệ");
        }
    }

    // Chuyển đổi WarrantyEntity thành WarrantyModel
    private WarrantyModel convertToModel(WarrantyEntity warranty) {
        if (warranty == null) {
            throw new BadRequestException("Dữ liệu bảo hành là null");
        }
        
        return WarrantyModel.builder()
            .id(warranty.getId())
            .wcode(warranty.getWcode())
            .userId(warranty.getUser().getId())
            .userEmail(warranty.getUser().getEmail())
            .userName(concatName(warranty.getUser()))
            .productId(warranty.getProduct().getId())
            .productName(warranty.getProduct().getName())
            .status(warranty.getStatus())
            .startDate(warranty.getStartDate())
            .endDate(warranty.getEndDate())
            .updatedAt(warranty.getUpdatedAt())
            .cancellationReason(warranty.getCancellationReason())
            .isActive(warranty.isActive())
            .build();
    }

    // Chuyển đổi tên người dùng - giữ nguyên vì không trùng lặp với controller
    private String concatName(UserEntity user) {
        if (user == null) {
            return "";
        }
        
        StringBuilder fullName = new StringBuilder();
        if (user.getFirstname() != null && !user.getFirstname().isEmpty()) {
            fullName.append(user.getFirstname());
        }
        
        if (user.getLastname() != null && !user.getLastname().isEmpty()) {
            if (fullName.length() > 0) {
                fullName.append(" ");
            }
            fullName.append(user.getLastname());
        }
        
        return fullName.length() > 0 ? fullName.toString() : user.getUsername();
    }
}