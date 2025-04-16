package hcmute.techshop.Mapper;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Model.User.ProfileRequest;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring", // Integrates with Spring if you're using it
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE // Skip null fields
)
public interface UserMapper {
    // Update existing UserEntity with data from another UserEntity
    void updateUserFromUser(@MappingTarget UserEntity target, ProfileRequest source);
}
