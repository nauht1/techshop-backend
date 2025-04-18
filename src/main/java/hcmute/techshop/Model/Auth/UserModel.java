package hcmute.techshop.Model.Auth;

import hcmute.techshop.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    private Integer id;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String phone;
    private boolean gender;
    private Role role;
    private String avatar;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
