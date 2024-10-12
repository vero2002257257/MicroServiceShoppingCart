package Emazon.MicroServiceShopCart.infrastructure.exception;

public class ExceptionResponseBuilder extends RuntimeException {
    public ExceptionResponseBuilder(String message) {
        super(message);
    }
}
