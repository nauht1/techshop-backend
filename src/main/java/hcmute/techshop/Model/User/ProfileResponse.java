package hcmute.techshop.Model.User;

import hcmute.techshop.Entity.Auth.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    String message;
    UserEntity user;
}
