package Emazon.MicroServiceShopCart.domain.spi;

import Emazon.MicroServiceShopCart.domain.models.ShoppingCart;

import java.util.Optional;

public interface IShoppingCartPersistencePort {
    ShoppingCart createShoppingCart(Long userId);
    Optional<ShoppingCart> getShoppingCartByUserId(Long userId);
    ShoppingCart getShoppingCartById(Long id);
    void updateCart(ShoppingCart shoppingCart);
}
