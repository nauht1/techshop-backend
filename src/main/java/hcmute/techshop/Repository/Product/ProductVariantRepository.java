package hcmute.techshop.Repository.Product;

import hcmute.techshop.Entity.Product.ProductVariantEntity;
import hcmute.techshop.Model.Product.ProductVariant.ProductVariantResponseProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariantEntity, Integer> {
    @Query("SELECT a.id AS id, a.variantName AS variantName, a.sku AS sku, a.price AS price, a.stock AS stock, b AS product FROM ProductVariantEntity a join ProductEntity b on a.product.id = b.id" )
    List<ProductVariantResponseProjection> findAllProject();
    ProductVariantResponseProjection findProjectById(Integer id);
    public List<ProductVariantResponseProjection> findAllProjectByProductId(int productId);
}
