package hcmute.techshop.Model.Order;

import hcmute.techshop.Entity.Order.OrderEntity;
import hcmute.techshop.Entity.Product.ProductEntity;
import hcmute.techshop.Model.Product.BrandModel;
import hcmute.techshop.Model.Product.ProductModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemModel {
    private Integer id;
    private Integer productId;
    private String productName;
    private String productImage;
    private Double productPrice;
    private Double productSalePrice;
    private Integer quantity;
    private Double unitPrice;
    private boolean isReviewed;
}
