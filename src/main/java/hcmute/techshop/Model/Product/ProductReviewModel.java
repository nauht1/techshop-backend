package hcmute.techshop.Model.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewModel {
    private Integer id;
    private Integer productId;
    private Integer userId;
    private String username;
    private String productName;
    private int rating;
    private String comment;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isOwner;
    private Integer orderItemId;
    
    // Getter và setter bổ sung cho isOwner vì tên thuộc tính không theo quy ước
    public boolean isOwner() {
        return isOwner;
    }
    
    public void setOwner(boolean owner) {
        isOwner = owner;
    }
}