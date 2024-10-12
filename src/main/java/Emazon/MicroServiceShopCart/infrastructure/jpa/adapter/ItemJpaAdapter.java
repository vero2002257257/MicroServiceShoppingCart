package Emazon.MicroServiceShopCart.infrastructure.jpa.adapter;
import Emazon.MicroServiceShopCart.domain.models.Item;
import Emazon.MicroServiceShopCart.domain.spi.ItemPersistencePort;
import Emazon.MicroServiceShopCart.infrastructure.exception.ItemNotFoundException;
import Emazon.MicroServiceShopCart.infrastructure.exception.NotShoppingCartFound;
import Emazon.MicroServiceShopCart.infrastructure.jpa.entity.ItemCartEntity;
import Emazon.MicroServiceShopCart.infrastructure.jpa.entity.ShoppingCartEntity;
import Emazon.MicroServiceShopCart.infrastructure.jpa.mapper.ItemCartEntityMapper;
import Emazon.MicroServiceShopCart.infrastructure.jpa.repository.ICartItemRepository;
import Emazon.MicroServiceShopCart.infrastructure.jpa.repository.IShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static Emazon.MicroServiceShopCart.utils.Constants.NOT_ITEM_SHOPPING_CART;
import static Emazon.MicroServiceShopCart.utils.Constants.NOT_SHOPPING_CART;

@Transactional
@RequiredArgsConstructor
public class ItemJpaAdapter implements ItemPersistencePort {

    private final ICartItemRepository itemRepository;
    private final ItemCartEntityMapper itemCartEntityMapper;
    private final IShoppingCartRepository shoppingCartRepository;

    @Override
    public Item save(Item item) {
        ItemCartEntity itemCartEntity = itemCartEntityMapper.toItemCartEntity(item);
        // Obtener el ShoppingCartEntity correspondiente
        ShoppingCartEntity shoppingCartEntity = shoppingCartRepository.findById(item.getShoppingCart().getId())
                .orElseThrow(() -> new NotShoppingCartFound(NOT_SHOPPING_CART));

        itemCartEntity.setShoppingCartEntity(shoppingCartEntity);
        shoppingCartEntity.getItems().add(itemCartEntity);

        itemRepository.save(itemCartEntity);
        return itemCartEntityMapper.toItem(itemCartEntity);
    }

    @Override
    public Item findById(Long id) {
        return itemRepository.findById(id)
                .map(itemCartEntityMapper::toItem)
                .orElseThrow(() -> new ItemNotFoundException(NOT_ITEM_SHOPPING_CART));
    }

    @Override
    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }
}