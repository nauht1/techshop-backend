package hcmute.techshop.Model.User;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequest {
    private String username;

    @Email(message = "Email không hợp lệ")
    private String email;

    private String firstname;
    private String lastname;
    private String phone;
    private Boolean gender;
}
