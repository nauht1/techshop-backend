package hcmute.techshop.Repository.Shipping;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Shipping.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Integer> {
    List<AddressEntity> findByUser(UserEntity user);
    Optional<AddressEntity> findByUserAndIsDefaultTrue(UserEntity user);
    Optional<AddressEntity> findByIdAndUser(Integer id, UserEntity user);
}