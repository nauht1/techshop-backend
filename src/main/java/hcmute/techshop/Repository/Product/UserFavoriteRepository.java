package hcmute.techshop.Repository.Product;

import hcmute.techshop.Entity.Product.UserFavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFavoriteRepository extends JpaRepository<UserFavoriteEntity, Integer> {
    List<UserFavoriteEntity> findByUserId(Integer userId);
    Optional<UserFavoriteEntity> findByUserIdAndProductId(Integer userId, Integer productId);
    void deleteByUserIdAndProductId(Integer userId, Integer productId);
}
