package hcmute.techshop.Repository.Cart;

import hcmute.techshop.Entity.Cart.CartEntity;
import hcmute.techshop.Entity.Cart.CartItemEntity;
import hcmute.techshop.Entity.Product.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Integer> {
    Optional<CartItemEntity> findByCartAndProduct(CartEntity cart, ProductEntity product);
}