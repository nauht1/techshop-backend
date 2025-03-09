package hcmute.techshop.Model.Cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCartItemRequest {
    private Integer cartItemId;
    private Integer quantity;
}