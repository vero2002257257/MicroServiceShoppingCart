package Emazon.MicroServiceShopCart.application.handler;

import Emazon.MicroServiceShopCart.application.dto.request.ItemRequest;

public interface ICartHandler {
    void addProductToCart(ItemRequest itemRequest, Long userId);
}
