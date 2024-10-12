package Emazon.MicroServiceShopCart.infrastructure.jpa.adapter;
import Emazon.MicroServiceShopCart.domain.models.ShoppingCart;
import Emazon.MicroServiceShopCart.domain.spi.IShoppingCartPersistencePort;
import Emazon.MicroServiceShopCart.infrastructure.exception.NotShoppingCartFound;
import Emazon.MicroServiceShopCart.infrastructure.jpa.entity.ItemCartEntity;
import Emazon.MicroServiceShopCart.infrastructure.jpa.entity.ShoppingCartEntity;
import Emazon.MicroServiceShopCart.infrastructure.jpa.mapper.ShoppingCartEntityMapper;
import Emazon.MicroServiceShopCart.infrastructure.jpa.repository.ICartItemRepository;
import Emazon.MicroServiceShopCart.infrastructure.jpa.repository.IShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static Emazon.MicroServiceShopCart.utils.Constants.NOT_SHOPPING_CART;


@Transactional
@RequiredArgsConstructor
public class ShoppingCartJpaAdapter implements IShoppingCartPersistencePort {

    private final IShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartEntityMapper shoppingCartEntityMapper;




    @Override
    public ShoppingCart createShoppingCart(Long userId) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        shoppingCart.setItems(new ArrayList<>());
        shoppingCart.setActualizationDate(LocalDateTime.now());

        ShoppingCartEntity shoppingCartEntity = shoppingCartEntityMapper.toEntity(shoppingCart);
        return shoppingCartEntityMapper.toShoppingCar(shoppingCartRepository.save(shoppingCartEntity));
    }

    @Override
    public Optional<ShoppingCart> getShoppingCartByUserId(Long userId) {
        return shoppingCartRepository.findByUserId(userId).map(shoppingCartEntityMapper::toShoppingCar);
    }


    @Override
    public ShoppingCart getShoppingCartById(Long id) {
        return shoppingCartRepository.findById(id)
                .map(shoppingCartEntityMapper::toShoppingCar)
                .orElseThrow(() -> new NotShoppingCartFound(NOT_SHOPPING_CART));
    }

    @Override
    public void updateCart(ShoppingCart shoppingCart) {
        ShoppingCartEntity shoppingCartEntity = shoppingCartEntityMapper.toEntityWithItems(shoppingCart);
        shoppingCartRepository.save(shoppingCartEntity);
    }

    @Override
    public void removeProduct(Long productId, Long userId) {
        ShoppingCart shoppingCart = getShoppingCartByUserId(userId)
                .orElseThrow(() -> new NotShoppingCartFound(NOT_SHOPPING_CART));
        updateCart(shoppingCart);
    }
}