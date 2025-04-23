package hcmute.techshop.Controller.Discount;

import hcmute.techshop.Model.Product.DiscountModel;
import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Service.Discount.IDiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/discounts")
@RequiredArgsConstructor
public class DiscountController {
    private final IDiscountService discountService;

    @GetMapping
    public ResponseEntity<ResponseModel> getActiveDiscount() {
        List<DiscountModel> discountModels = discountService.getAllDiscountsActive(true);
        return ResponseEntity.ok(new ResponseModel(true, "Lấy danh sách discount thành công", discountModels));
    }
}
