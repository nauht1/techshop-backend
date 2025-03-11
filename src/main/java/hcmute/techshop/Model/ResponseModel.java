package hcmute.techshop.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResponseModel {
    private boolean success;
    private String message;
    private Object body;
}

/*
* {
  "success": true,
  "message": "Get data successfully",
  "data": {
    "id": 1,
    "name": "iPhone 15",
    "price": 25000000
  }
}
* */
