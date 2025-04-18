package hcmute.techshop.Controller.Discount;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Model.Cart.CartResponse;
import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Service.Discount.IDiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/admin/discounts")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")  // Chỉ admin mới có quyền truy cập APIs này
public class AdminDiscountController {
    @Autowired
    private IDiscountService discountService;
    @GetMapping("/search")
    public ResponseEntity<ResponseModel> getDiscountList(
            @RequestParam(name="keyword",required = false) String keyword,
            @RequestParam(name="startValue",required = false) Integer startValue,
            @RequestParam(name="endValue", required = false) Integer endValue,
            @RequestParam(name="startDate",required = false) LocalDateTime startDate,
            @RequestParam(name="endDate",required = false) LocalDateTime endDate,
            @RequestParam(name="pageNumber",required = false)Integer pageNumber,
            @RequestParam(name="pageSize",required = false) Integer pageSize,
            @RequestParam(name="sortBy",required = false) String sortBy
    ) {
        ResponseModel resp = discountService.getDiscountList(keyword,startValue,endValue,startDate,endDate,pageNumber,pageSize,sortBy);
        return ResponseEntity.ok(resp);
    }
    @PostMapping("/add")
    public ResponseEntity<ResponseModel> addNewDiscount(
            @RequestParam(name="code") String code,
            @RequestParam(name="quantity",required = false) Integer quantity,
            @RequestParam(name="value", required = false) Double value,
            @RequestParam(name="startDate",required = false) LocalDateTime startDate,
            @RequestParam(name = "endDate",required = false) LocalDateTime endDate
    ) {
        ResponseModel resp = discountService.addNewDiscount(code,quantity,value,startDate,endDate);
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseModel> updateDiscount(
            @RequestParam(name="id") Integer id,
            @RequestParam(name="code",required = false) String code,
            @RequestParam(name="quantity",required = false) Integer quantity,
            @RequestParam(name="value", required = false) Double value,
            @RequestParam(name="startDate",required = false) LocalDateTime startDate,
            @RequestParam(name = "endDate",required = false) LocalDateTime endDate
    ) {
        ResponseModel resp = discountService.updateDiscount(id,code,quantity,value,startDate,endDate);
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/del")
    public ResponseEntity<ResponseModel> deleteDiscount(
            @RequestParam(name="id") Integer id
    ) {
        ResponseModel resp = discountService.deleteDiscount(id);
        return ResponseEntity.ok(resp);
    }
}
