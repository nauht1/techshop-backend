package hcmute.techshop.Service.Cart;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Cart.CartEntity;
import hcmute.techshop.Entity.Cart.CartItemEntity;
import hcmute.techshop.Entity.Product.ProductEntity;
import hcmute.techshop.Enum.EventType;
import hcmute.techshop.Exception.ResourceNotFoundException;
import hcmute.techshop.Model.Cart.*;
import hcmute.techshop.Repository.Cart.CartItemRepository;
import hcmute.techshop.Repository.Cart.CartRepository;
import hcmute.techshop.Repository.Product.ProductRepository;
import hcmute.techshop.Service.Tracking.ITrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final ITrackingService trackingService;

    @Override
    @Transactional
    public CartResponse addToCart(UserEntity user, AddToCartRequest request) {
        CartEntity cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    CartEntity newCart = new CartEntity();
                    newCart.setUser(user);
                    newCart.setCartItems(new ArrayList<>());
                    return cartRepository.save(newCart);
                });

        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));

        CartItemEntity cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseGet(() -> {
                    CartItemEntity newItem = new CartItemEntity();
                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    newItem.setQuantity(0);
                    newItem.setChecked(true);
                    cart.getCartItems().add(newItem);
                    return newItem;
                });

        cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        cartItemRepository.save(cartItem);
        trackingService.track(user, EventType.ADD_TO_CART, "add to cart with id: " + cart.getId().toString());
        return buildCartResponse(cart);
    }

    @Override
    public CartResponse getCart(UserEntity user) {
        CartEntity cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    CartEntity newCart = new CartEntity();
                    newCart.setUser(user);
                    newCart.setCartItems(new ArrayList<>());
                    return cartRepository.save(newCart);
                });

        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse removeFromCart(UserEntity user, Integer cartItemId) {
        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giỏ hàng"));

        CartItemEntity cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm trong giỏ hàng"));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new IllegalArgumentException("Sản phẩm không thuộc giỏ hàng của người dùng này");
        }

        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse updateCartItem(UserEntity user, UpdateCartItemRequest request) {
        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giỏ hàng"));

        CartItemEntity cartItem = cartItemRepository.findById(request.getCartItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm trong giỏ hàng"));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new IllegalArgumentException("Sản phẩm không thuộc giỏ hàng của người dùng này");
        }

        cartItem.setQuantity(request.getQuantity());

        if (request.getQuantity() <= 0) {
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        } else {
            cartItemRepository.save(cartItem);
        }

        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse selectAllItems(UserEntity user) {
        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giỏ hàng"));

        cart.getCartItems().forEach(item -> item.setChecked(true));
        cartRepository.save(cart);

        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse deselectAllItems(UserEntity user) {
        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giỏ hàng"));

        cart.getCartItems().forEach(item -> item.setChecked(false));
        cartRepository.save(cart);

        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse clearCart(UserEntity user) {
        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giỏ hàng"));

        // Delete all cart items
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        cartRepository.save(cart);

        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse incrementCartItemQuantity(UserEntity user, Integer cartItemId) {
        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giỏ hàng"));

        CartItemEntity cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm trong giỏ hàng"));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new IllegalArgumentException("Sản phẩm không thuộc giỏ hàng của người dùng này");
        }

        // Get product to check stock
        ProductEntity product = cartItem.getProduct();
        if (product.getStock() > cartItem.getQuantity()) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItemRepository.save(cartItem);
        } else {
            throw new IllegalArgumentException("Số lượng sản phẩm đã đạt giới hạn tồn kho");
        }

        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse decrementCartItemQuantity(UserEntity user, Integer cartItemId) {
        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giỏ hàng"));

        CartItemEntity cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm trong giỏ hàng"));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new IllegalArgumentException("Sản phẩm không thuộc giỏ hàng của người dùng này");
        }

        if (cartItem.getQuantity() <= 1) {
            // Remove item if quantity will be 0
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartItemRepository.save(cartItem);
        }

        return buildCartResponse(cart);
    }

    private CartResponse buildCartResponse(CartEntity cart) {
        List<CartItemResponse> itemResponses = cart.getCartItems().stream()
                .map(item -> {
                    ProductEntity product = item.getProduct();
                    Double subtotal = product.getPrice() * item.getQuantity();

                    return CartItemResponse.builder()
                            .id(item.getId())
                            .productId(product.getId())
                            .productName(product.getName())
                            .productPrice(product.getPrice())
                            .quantity(item.getQuantity())
                            .subtotal(subtotal)
                            .isChecked(item.isChecked())
                            .build();
                })
                .collect(Collectors.toList());

        Double totalPrice = itemResponses.stream()
                .filter(CartItemResponse::isChecked)
                .mapToDouble(CartItemResponse::getSubtotal)
                .sum();

        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .items(itemResponses)
                .totalPrice(totalPrice)
                .build();
    }

    @Override
    @Transactional
    public CartResponse toggleCartItem(UserEntity user, ToggleCartItemRequest request) {
        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giỏ hàng"));

        CartItemEntity cartItem = cartItemRepository.findById(request.getCartItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm trong giỏ hàng"));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new IllegalArgumentException("Sản phẩm không thuộc giỏ hàng của người dùng này");
        }

        cartItem.setChecked(request.isChecked());
        cartItemRepository.save(cartItem);

        return buildCartResponse(cart);
    }
}