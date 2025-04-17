package hcmute.techshop.Config;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Product.BrandEntity;
import hcmute.techshop.Entity.Product.CategoryEntity;
import hcmute.techshop.Entity.Product.ProductEntity;
import hcmute.techshop.Enum.Role;
import hcmute.techshop.Repository.Auth.UserRepository;
import hcmute.techshop.Repository.Product.BrandRepository;
import hcmute.techshop.Repository.Product.CategoryRepository;
import hcmute.techshop.Repository.Product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        createDefaultAdminIfNotExists();
        createDefaultUserIfNotExists();
        createSampleProductsIfNotExists();
    }

    private void createDefaultAdminIfNotExists() {
        String adminEmail = "admin@techshop.com";
        Optional<UserEntity> existingAdmin = userRepository.findByEmail(adminEmail);

        if (existingAdmin.isEmpty()) {
            UserEntity admin = UserEntity.builder()
                    .email(adminEmail)
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .firstname("Admin")
                    .lastname("User")
                    .role(Role.ROLE_ADMIN)
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            userRepository.save(admin);
            System.out.println("Created default admin account: admin@techshop.com / admin123");
        }
    }

    private void createDefaultUserIfNotExists() {
        String userEmail = "user@techshop.com";
        Optional<UserEntity> existingUser = userRepository.findByEmail(userEmail);

        if (existingUser.isEmpty()) {
            UserEntity user = UserEntity.builder()
                    .email(userEmail)
                    .username("user")
                    .password(passwordEncoder.encode("user123"))
                    .firstname("Normal")
                    .lastname("User")
                    .role(Role.ROLE_CUSTOMER)
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            userRepository.save(user);
            System.out.println("Created default user account: user@techshop.com / user123");
        }

        String userEmail1 = "user1@techshop.com";
        Optional<UserEntity> existingUser1 = userRepository.findByEmail(userEmail1);

        if (existingUser1.isEmpty()) {
            UserEntity user1 = UserEntity.builder()
                    .email(userEmail1)
                    .username("user1")
                    .password(passwordEncoder.encode("user123"))
                    .firstname("Normal")
                    .lastname("User1")
                    .role(Role.ROLE_CUSTOMER)
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            userRepository.save(user1);
            System.out.println("Created another default user account: user1@techshop.com / user123");
        }
    }

    private void createSampleProductsIfNotExists() {
        if (productRepository.count() > 0) {
            return;
        }

        List<CategoryEntity> categories = createSampleCategories();

        List<BrandEntity> brands = createSampleBrands();

        createSampleProducts(categories, brands);

        System.out.println("Created 10 sample products");
    }

    private List<CategoryEntity> createSampleCategories() {
        List<String> categoryNames = Arrays.asList(
                "Điện thoại", "Laptop", "Máy tính bảng", "Tai nghe", "Phụ kiện"
        );

        return categoryNames.stream()
                .map(name -> {
                    CategoryEntity category = new CategoryEntity();
                    category.setName(name);
                    category.setActive(true);
                    return categoryRepository.save(category);
                })
                .toList();
    }

    private List<BrandEntity> createSampleBrands() {
        List<String[]> brandData = Arrays.asList(
                new String[]{"Apple", "USA"},
                new String[]{"Samsung", "Korea"},
                new String[]{"Xiaomi", "China"},
                new String[]{"Dell", "USA"},
                new String[]{"Asus", "Taiwan"}
        );

        return brandData.stream()
                .map(data -> {
                    BrandEntity brand = new BrandEntity();
                    brand.setName(data[0]);
                    brand.setCountry(data[1]);
                    brand.setActive(true);
                    return brandRepository.save(brand);
                })
                .toList();
    }

    private void createSampleProducts(List<CategoryEntity> categories, List<BrandEntity> brands) {
        ProductEntity[] sampleProducts = {
                createProduct("iPhone 15 Pro", "Smartphone cao cấp mới nhất của Apple", 29990000.0, 28990000.0, 50, categories.get(0), brands.get(0)),
                createProduct("Samsung Galaxy S23", "Smartphone Android flagship", 22990000.0, 21990000.0, 45, categories.get(0), brands.get(1)),
                createProduct("Xiaomi 13", "Smartphone tầm trung cấu hình cao", 16990000.0, 15990000.0, 60, categories.get(0), brands.get(2)),
                createProduct("MacBook Pro M2", "Laptop mạnh mẽ với chip M2", 35990000.0, 34990000.0, 30, categories.get(1), brands.get(0)),
                createProduct("Dell XPS 15", "Laptop đồ họa cao cấp", 42990000.0, 41990000.0, 25, categories.get(1), brands.get(3)),
                createProduct("Asus ROG Strix", "Laptop gaming hiệu năng cao", 38990000.0, 37490000.0, 20, categories.get(1), brands.get(4)),
                createProduct("iPad Pro", "Máy tính bảng cao cấp của Apple", 23990000.0, 22990000.0, 40, categories.get(2), brands.get(0)),
                createProduct("Samsung Galaxy Tab S9", "Máy tính bảng Android cao cấp", 19990000.0, 18990000.0, 35, categories.get(2), brands.get(1)),
                createProduct("AirPods Pro", "Tai nghe không dây chống ồn", 5990000.0, 5490000.0, 100, categories.get(3), brands.get(0)),
                createProduct("Samsung Galaxy Buds Pro", "Tai nghe không dây chống ồn", 4990000.0, 4490000.0, 90, categories.get(3), brands.get(1))
        };


        for (ProductEntity product : sampleProducts) {
            productRepository.save(product);
        }
    }

    private ProductEntity createProduct(String name, String description, Double price, Double salePrice, Integer stock, CategoryEntity category, BrandEntity brand) {
        ProductEntity product = new ProductEntity();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setSalePrice(salePrice);
        product.setStock(stock);
        product.setActive(true);
        product.setCategory(category);
        product.setBrand(brand);
        return product;
    }
}