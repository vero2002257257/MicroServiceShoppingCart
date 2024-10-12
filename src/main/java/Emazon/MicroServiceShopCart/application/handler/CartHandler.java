package Emazon.MicroServiceShopCart.application.handler;

import Emazon.MicroServiceShopCart.application.dto.request.ItemRequest;
import Emazon.MicroServiceShopCart.application.mapper.ProductRequestMapper;
import Emazon.MicroServiceShopCart.domain.api.IShoppingCartServicePort;
import Emazon.MicroServiceShopCart.domain.models.Item;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class CartHandler implements ICartHandler{

    private final IShoppingCartServicePort shoppingCarServicePort;
    private final ProductRequestMapper productRequestMapper;


    @Override
    public void addProductToCart(ItemRequest itemRequest, Long userId) {
        Item item = productRequestMapper.toItem(itemRequest);
        shoppingCarServicePort.addProduct(item, userId);
    }
}