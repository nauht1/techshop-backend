package hcmute.techshop.Service.Discount;

import hcmute.techshop.Entity.Product.DiscountEntity;
import hcmute.techshop.Model.ResponseModel;

import java.time.LocalDateTime;

public interface IDiscountService {
    ResponseModel getDiscountList(String keyword, Integer startValue, Integer endValue,
                                  LocalDateTime startDate, LocalDateTime endDate,
                                  Integer pageNumber, Integer pageSize, String sortBy);

    ResponseModel addNewDiscount(String code, Integer quantity, Double value,
                                 LocalDateTime startDate, LocalDateTime endDate);

    ResponseModel updateDiscount(Integer id, String code, Integer quantity, Double value,
                                 LocalDateTime startDate, LocalDateTime endDate);

    ResponseModel deleteDiscount(Integer id);
}
