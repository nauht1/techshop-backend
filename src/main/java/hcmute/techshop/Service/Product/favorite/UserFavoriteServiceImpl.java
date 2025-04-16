package hcmute.techshop.Service.Product.favorite;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Product.ProductEntity;
import hcmute.techshop.Entity.Product.UserFavoriteEntity;
import hcmute.techshop.Exception.ResourceNotFoundException;
import hcmute.techshop.Model.Product.ProductModel;
import hcmute.techshop.Repository.Auth.UserRepository;
import hcmute.techshop.Repository.Product.ProductRepository;
import hcmute.techshop.Repository.Product.UserFavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserFavoriteServiceImpl implements IUserFavoriteService {
    private final UserFavoriteRepository userFavoriteRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public List<ProductModel> getFavoritesByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        UserEntity user = (UserEntity) authentication.getPrincipal();

        List<UserFavoriteEntity> favorites = userFavoriteRepository.findByUserId(user.getId());
        return favorites.stream().map(fav -> {
            var p = fav.getProduct();
            return ProductModel.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .description(p.getDescription())
                    .price(p.getPrice())
                    .salePrice(p.getSalePrice())
                    .stock(p.getStock())
                    .isActive(p.isActive())
                    .categoryId(p.getCategory().getId())
                    .brandId(p.getBrand().getId())
                    .build();
        }).toList();
    }

    @Override
    public ProductModel addFavorite(Integer productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        UserEntity user = (UserEntity) authentication.getPrincipal();

        if (userFavoriteRepository.findByUserIdAndProductId(user.getId(), productId).isPresent()) {
            throw new IllegalArgumentException("Sản phẩm đã được yêu thích");
        }

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));

        UserFavoriteEntity favorite = UserFavoriteEntity.builder()
                .user(user)
                .product(product)
                .build();

        userFavoriteRepository.save(favorite);

        return ProductModel.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .salePrice(product.getSalePrice())
                .stock(product.getStock())
                .isActive(product.isActive())
                .categoryId(product.getCategory().getId())
                .brandId(product.getBrand().getId())
                .build();
    }

    @Override
    public void removeFavorite(Integer productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return;
        }
        UserEntity user = (UserEntity) authentication.getPrincipal();

        UserFavoriteEntity favorite = userFavoriteRepository.findByUserIdAndProductId(user.getId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));

        userFavoriteRepository.delete(favorite);
    }
}
