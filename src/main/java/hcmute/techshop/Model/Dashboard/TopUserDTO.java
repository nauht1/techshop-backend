package hcmute.techshop.Model.Dashboard;

import hcmute.techshop.Model.Auth.UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TopUserDTO {
    UserModel user;
    Double amountSpent;
}
