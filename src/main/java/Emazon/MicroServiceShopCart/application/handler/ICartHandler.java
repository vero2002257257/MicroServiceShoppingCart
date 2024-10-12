package Emazon.MicroServiceShopCart.application.handler;

import Emazon.MicroServiceShopCart.application.dto.request.ItemRequest;
import Emazon.MicroServiceShopCart.application.dto.response.ShoppingCartItemResponse;
import Emazon.MicroServiceShopCart.domain.pagination.PageCustom;
import Emazon.MicroServiceShopCart.domain.pagination.PageRequestCustom;

public interface ICartHandler {
    void addProductToCart(ItemRequest itemRequest, Long userId);
    void removeProduct(Long productId, Long userId);
    PageCustom<ShoppingCartItemResponse> getCartItems(Long userId, PageRequestCustom pageRequest, String categoryName, String brandName);
}

