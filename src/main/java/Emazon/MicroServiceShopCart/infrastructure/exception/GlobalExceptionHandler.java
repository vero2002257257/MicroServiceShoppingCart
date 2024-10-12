package Emazon.MicroServiceShopCart.infrastructure.exception;
import Emazon.MicroServiceShopCart.domain.exception.InvalidQuantityProducts;
import Emazon.MicroServiceShopCart.domain.exception.OutOfStockException;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.ControllerAdvice;

import static Emazon.MicroServiceShopCart.utils.Constants.PRODUCT_NOT_FOUND;
import static Emazon.MicroServiceShopCart.utils.Constants.UNEXPECTED_ERROR;

@ControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {

    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<Object> handleOutOfStockException(OutOfStockException ex) {
        return new ResponseEntity<>(ExceptionResponseBuilder.buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidQuantityProducts.class)
    public ResponseEntity<Object> handleInvalidQuantityProducts(InvalidQuantityProducts ex) {
        return new ResponseEntity<>(ExceptionResponseBuilder.buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<Object> handleItemNotFoundException(ItemNotFoundException ex) {
        return new ResponseEntity<>(ExceptionResponseBuilder.buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<Object> handleNotAuthorizedException(NotAuthorizedException ex) {
        return new ResponseEntity<>(ExceptionResponseBuilder.buildResponse(ex.getMessage(), HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException ex) {
        return new ResponseEntity<>(ExceptionResponseBuilder.buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<Object> handleFeignProductNotFoundException() {
        return new ResponseEntity<>(ExceptionResponseBuilder.buildResponse(PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(FeignException.InternalServerError.class)
    public ResponseEntity<Object> handleFeignInternalServerError() {
        return new ResponseEntity<>(ExceptionResponseBuilder.buildResponse(UNEXPECTED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
