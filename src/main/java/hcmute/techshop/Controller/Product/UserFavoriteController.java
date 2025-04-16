package hcmute.techshop.Controller.Product;

import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Service.Product.favorite.IUserFavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product/favorites")
@RequiredArgsConstructor
public class UserFavoriteController {
    private final IUserFavoriteService favoriteService;

    @GetMapping
    public ResponseEntity<ResponseModel> getFavorites() {
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .success(true)
                        .message("Lấy danh sách yêu thích thành công")
                        .body(favoriteService.getFavoritesByUserId())
                        .build()
        );
    }

    @PostMapping("/add/{productId}")
    public ResponseEntity<ResponseModel> addFavorite(@PathVariable Integer productId) {
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .success(true)
                        .message("Đã thêm vào danh sách yêu thích")
                        .body(favoriteService.addFavorite(productId))
                        .build()
        );
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ResponseModel> removeFavorite(@PathVariable Integer productId) {
        favoriteService.removeFavorite(productId);
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .success(true)
                        .message("Đã xoá khỏi danh sách yêu thích")
                        .body(null)
                        .build()
        );
    }
}
