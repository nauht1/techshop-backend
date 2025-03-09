package hcmute.techshop.Repository.Cart;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Cart.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Integer> {
    Optional<CartEntity> findByUser(UserEntity user);
}