package hcmute.techshop.Controller.Discount;

import hcmute.techshop.Model.ResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/discounts")
public class DiscountController {
    @GetMapping("/all")
    public ResponseEntity<ResponseModel> getActiveDiscount() {
//        CartResponse cart = cartService.getCart(user);

        return ResponseEntity.ok(new ResponseModel(true, "Lấy thông tin giỏ hàng thành công", null));
    }
}
