package hcmute.techshop.Controller.Cart;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Model.Cart.AddToCartRequest;
import hcmute.techshop.Model.Cart.CartResponse;
import hcmute.techshop.Model.Cart.UpdateCartItemRequest;
import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Service.Cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final ICartService cartService;

    @GetMapping
    public ResponseEntity<ResponseModel> getCart(@AuthenticationPrincipal UserEntity user) {
        CartResponse cart = cartService.getCart(user);
        return ResponseEntity.ok(new ResponseModel(true, "Lấy thông tin giỏ hàng thành công", cart));
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseModel> addToCart(@AuthenticationPrincipal UserEntity user, @RequestBody AddToCartRequest request) {
        CartResponse updatedCart = cartService.addToCart(user, request);
        return ResponseEntity.ok(new ResponseModel(true, "Thêm sản phẩm vào giỏ hàng thành công", updatedCart));
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<ResponseModel> removeFromCart(@AuthenticationPrincipal UserEntity user, @PathVariable Integer cartItemId) {
        CartResponse updatedCart = cartService.removeFromCart(user, cartItemId);
        return ResponseEntity.ok(new ResponseModel(true, "Xóa sản phẩm khỏi giỏ hàng thành công", updatedCart));
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseModel> updateCartItem(@AuthenticationPrincipal UserEntity user, @RequestBody UpdateCartItemRequest request) {
        CartResponse updatedCart = cartService.updateCartItem(user, request);
        return ResponseEntity.ok(new ResponseModel(true, "Cập nhật giỏ hàng thành công", updatedCart));
    }
}