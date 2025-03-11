package hcmute.techshop.Model.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddReviewRequest {
    private Integer productId;
    
    private Integer rating;
    
    private String comment;
} 