package hcmute.techshop.Model.Shipping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressModel {
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
