package hcmute.techshop.Model.Cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToggleCartItemRequest {
    private Integer cartItemId;
    private boolean checked;
}