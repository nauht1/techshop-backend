package hcmute.techshop.Service.Discount;

import hcmute.techshop.Entity.Product.DiscountEntity;
import hcmute.techshop.Model.PageResponse;
import hcmute.techshop.Model.Product.Discount.AdminDiscountResponse;
import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Repository.Order.DiscountRepository;
import hcmute.techshop.Repository.Order.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements IDiscountService {
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public ResponseModel getDiscountList(String keyword, Integer startValue, Integer endValue,
                                         LocalDateTime startDate, LocalDateTime endDate,
                                         Integer pageNumber, Integer pageSize, String sortBy) {
        List<DiscountEntity> allDiscounts = discountRepository.findAll(); // Tạm thời chưa lọc bằng query

        List<DiscountEntity> filtered = allDiscounts.stream()
                .filter(d -> keyword == null || d.getCode().toLowerCase().contains(keyword.toLowerCase()))
                .filter(d -> startValue == null || d.getValue() >= startValue)
                .filter(d -> endValue == null || d.getValue() <= endValue)
                .filter(d -> startDate == null || !d.getStartDate().isBefore(startDate))
                .filter(d -> endDate == null || !d.getEndDate().isAfter(endDate))
                .toList();

        // Pagination
        pageNumber = pageNumber == null ? 0 : pageNumber;
        pageSize = pageSize == null ? 10 : pageSize;
        int start = pageNumber * pageSize;
        int end = Math.min(start + pageSize, filtered.size());
        List<DiscountEntity> pageContent = filtered.subList(start, end);
        List<AdminDiscountResponse> discountResponses = pageContent.stream()
                .map(obj -> modelMapper.map(obj, AdminDiscountResponse.class))
                .collect(Collectors.toList());
        int totalPages = (int) Math.ceil((double) filtered.size() / pageSize);

        PageResponse<AdminDiscountResponse> response = new PageResponse<>(discountResponses,pageNumber+1,totalPages);
        return new ResponseModel(true, "Lấy danh sách discount thành công", response);
    }

    @Override
    @Transactional
    public ResponseModel addNewDiscount(String code, Integer quantity, Double value,
                                        LocalDateTime startDate, LocalDateTime endDate) {
        DiscountEntity discount = new DiscountEntity();
        discount.setCode(code);
        if(quantity!=null)
            discount.setQuantity(quantity);
        discount.setValue(value);
        if(startDate!=null)
            discount.setStartDate(startDate);
        if(endDate!=null)
            discount.setEndDate(endDate);
        discount.setActive(true);

        discountRepository.save(discount);
        return new ResponseModel(true, "Thêm mã giảm giá thành công", discount);
    }

    @Override
    @Transactional
    public ResponseModel updateDiscount(Integer id, String code, Integer quantity, Double value,
                                        LocalDateTime startDate, LocalDateTime endDate) {
        Optional<DiscountEntity> opt = discountRepository.findById(id);
        if (opt.isEmpty()) {
            return new ResponseModel(false, "Không tìm thấy mã giảm giá với id: " + id, null);
        }

        DiscountEntity discount = opt.get();
        discount.setCode(code != null ? code : discount.getCode());
        discount.setQuantity(quantity != null ? quantity : discount.getQuantity());
        discount.setValue(value != null ? value : discount.getValue());
        discount.setStartDate(startDate != null ? startDate : discount.getStartDate());
        discount.setEndDate(endDate != null ? endDate : discount.getEndDate());

        discountRepository.save(discount);
        return new ResponseModel(true, "Cập nhật mã giảm giá thành công", discount);
    }

    @Override
    @Transactional
    public ResponseModel deleteDiscount(Integer id) {
        Optional<DiscountEntity> opt = discountRepository.findById(id);
        if (opt.isEmpty()) {
            return new ResponseModel(false, "Không tìm thấy mã giảm giá với id: " + id, null);
        }

        DiscountEntity discount = opt.get();
        discount.setActive(false); // set inactive thay vì xóa
        discountRepository.save(discount);
        return new ResponseModel(true, "Xóa mã giảm giá thành công", null);
    }
}
