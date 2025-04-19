package hcmute.techshop.Repository.Product;

import hcmute.techshop.Entity.Product.BrandEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Integer> {
    List<BrandEntity> findAllByIsActiveTrue();
}