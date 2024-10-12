package Emazon.MicroServiceShopCart.domain.exception;

public class InvalidQuantityProducts extends RuntimeException {
    public InvalidQuantityProducts(String message) {
        super(message);
    }
}
