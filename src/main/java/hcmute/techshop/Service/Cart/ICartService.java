package hcmute.techshop.Service.Cart;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Model.Cart.AddToCartRequest;
import hcmute.techshop.Model.Cart.CartResponse;
import hcmute.techshop.Model.Cart.ToggleCartItemRequest;
import hcmute.techshop.Model.Cart.UpdateCartItemRequest;

public interface ICartService {
    CartResponse addToCart(UserEntity user, AddToCartRequest request);
    CartResponse getCart(UserEntity user);
    CartResponse removeFromCart(UserEntity user, Integer cartItemId);
    CartResponse updateCartItem(UserEntity user, UpdateCartItemRequest request);

    CartResponse selectAllItems(UserEntity user);
    CartResponse deselectAllItems(UserEntity user);
    CartResponse clearCart(UserEntity user);
    CartResponse incrementCartItemQuantity(UserEntity user, Integer cartItemId);
    CartResponse decrementCartItemQuantity(UserEntity user, Integer cartItemId);
    CartResponse toggleCartItem(UserEntity user, ToggleCartItemRequest request);
}