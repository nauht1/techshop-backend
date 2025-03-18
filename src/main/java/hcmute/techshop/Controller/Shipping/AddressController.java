package hcmute.techshop.Controller.Shipping;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Model.Shipping.AddressRequestDTO;
import hcmute.techshop.Service.Shipping.IAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final IAddressService addressService;

    @GetMapping
    public ResponseEntity<ResponseModel> getUserAddresses(@AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(new ResponseModel(
                true,
                "Lấy danh sách địa chỉ thành công",
                addressService.getUserAddresses(user)
        ));
    }

    @PostMapping
    public ResponseEntity<ResponseModel> addAddress(
            @AuthenticationPrincipal UserEntity user,
            @RequestBody AddressRequestDTO request
    ) {
        return ResponseEntity.ok(new ResponseModel(
                true,
                "Thêm địa chỉ mới thành công",
                addressService.addAddress(user, request)
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseModel> updateAddress(
            @AuthenticationPrincipal UserEntity user,
            @PathVariable Integer id,
            @RequestBody AddressRequestDTO request
    ) {
        return ResponseEntity.ok(new ResponseModel(
                true,
                "Cập nhật địa chỉ thành công",
                addressService.updateAddress(user, id, request)
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseModel> removeAddress(
            @AuthenticationPrincipal UserEntity user,
            @PathVariable Integer id
    ) {
        addressService.removeAddress(user, id);
        return ResponseEntity.ok(new ResponseModel(
                true,
                "Xóa địa chỉ thành công",
                null
        ));
    }

    @PutMapping("/{id}/default")
    public ResponseEntity<ResponseModel> setDefaultAddress(
            @AuthenticationPrincipal UserEntity user,
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(new ResponseModel(
                true,
                "Đặt địa chỉ mặc định thành công",
                addressService.setDefaultAddress(user, id)
        ));
    }
}