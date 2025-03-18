package hcmute.techshop.Model.Shipping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressResponseDTO {
    private Integer id;
    private String street;
    private String ward;
    private String district;
    private String province;
    private boolean isDefault;
    private Integer shippingMethodId;
    private String shippingMethodName;
}