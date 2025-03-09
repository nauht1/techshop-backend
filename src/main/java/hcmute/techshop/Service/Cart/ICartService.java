package hcmute.techshop.Service.Cart;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Model.Cart.AddToCartRequest;
import hcmute.techshop.Model.Cart.CartResponse;
import hcmute.techshop.Model.Cart.UpdateCartItemRequest;

public interface ICartService {
    CartResponse addToCart(UserEntity user, AddToCartRequest request);
    CartResponse getCart(UserEntity user);
    CartResponse removeFromCart(UserEntity user, Integer cartItemId);
    CartResponse updateCartItem(UserEntity user, UpdateCartItemRequest request);
}