package Emazon.MicroServiceShopCart.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


@Getter
@Setter
public class ItemRequest {
    @NotNull
    private Long productId;
    @NotNull
    @Positive
    private int quantity;
}