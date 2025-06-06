package hcmute.techshop.Repository.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hcmute.techshop.Entity.Product.ProductAttributeEntity;
import hcmute.techshop.Model.Product.ProductAttribute.ProductAttributeResponseProjection;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttributeEntity, Integer> {
    @Query("SELECT a.id AS id, a.attName AS attName, a.attValue AS attValue, b as product  FROM ProductAttributeEntity a join ProductEntity b on a.product.id = b.id")
    List<ProductAttributeResponseProjection> findAllProject();
    public ProductAttributeResponseProjection findProjectById(Integer id);
    public List<ProductAttributeResponseProjection> findAllProjectByProductId(int id);
    List<ProductAttributeEntity> findByProductId(int productId);
}
