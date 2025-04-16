package hcmute.techshop.Service.Product.favorite;

import hcmute.techshop.Model.Product.ProductModel;

import java.util.List;

public interface IUserFavoriteService {
    List<ProductModel> getFavoritesByUserId();
    ProductModel addFavorite(Integer productId);
    void removeFavorite(Integer productId);
}
