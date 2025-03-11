package hcmute.techshop.Model.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCommentRequest {
    private Integer productId;
    
    private String comment;
    
    public String getComment() {
        return comment != null ? comment.trim() : null;
    }
} 