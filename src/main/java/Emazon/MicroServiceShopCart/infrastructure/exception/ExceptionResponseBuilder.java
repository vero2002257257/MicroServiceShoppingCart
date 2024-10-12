package Emazon.MicroServiceShopCart.infrastructure.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static Emazon.MicroServiceShopCart.utils.Constants.*;

public class ExceptionResponseBuilder extends RuntimeException {
    public static Map<String, Object> buildResponse(String message, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(MESSAGE, message);
        body.put(STATUS, status.value());
        return body;
    }
}
