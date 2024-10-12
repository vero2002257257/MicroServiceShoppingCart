package Emazon.MicroServiceShopCart.domain.spi;


import Emazon.MicroServiceShopCart.domain.models.Item;

public interface ItemPersistencePort {
    Item save(Item item);
    Item findById(Long id);
    void deleteById(Long id);
}