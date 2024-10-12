package Emazon.MicroServiceShopCart.domain.api;


import Emazon.MicroServiceShopCart.domain.models.CartItems;
import Emazon.MicroServiceShopCart.domain.models.Item;
import Emazon.MicroServiceShopCart.domain.pagination.PageCustom;
import Emazon.MicroServiceShopCart.domain.pagination.PageRequestCustom;

public interface IShoppingCartServicePort {
    void addProduct(Item item, Long userId);
    void removeProduct(Long productId, Long userId);
    PageCustom<CartItems> getPaginatedCartItems(Long userId, PageRequestCustom pageRequest, String categoryName, String brandName);
}
