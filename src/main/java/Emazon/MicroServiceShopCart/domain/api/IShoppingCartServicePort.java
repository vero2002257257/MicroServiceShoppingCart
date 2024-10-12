package Emazon.MicroServiceShopCart.domain.api;


import Emazon.MicroServiceShopCart.domain.models.Item;

public interface IShoppingCartServicePort {
    void addProduct(Item item, Long userId);
}