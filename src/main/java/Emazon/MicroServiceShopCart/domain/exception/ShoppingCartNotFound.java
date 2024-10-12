package Emazon.MicroServiceShopCart.domain.exception;

public class ShoppingCartNotFound extends RuntimeException {
    public ShoppingCartNotFound(String message) {
        super(message);
    }
}
