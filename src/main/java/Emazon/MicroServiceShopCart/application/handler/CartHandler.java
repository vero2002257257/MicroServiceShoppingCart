package Emazon.MicroServiceShopCart.application.handler;

import Emazon.MicroServiceShopCart.application.dto.request.ItemRequest;
import Emazon.MicroServiceShopCart.application.dto.response.ShoppingCartItemResponse;
import Emazon.MicroServiceShopCart.application.mapper.ProductRequestMapper;
import Emazon.MicroServiceShopCart.application.mapper.ShoppingCartItemResponseMapper;
import Emazon.MicroServiceShopCart.domain.api.IShoppingCartServicePort;
import Emazon.MicroServiceShopCart.domain.models.CartItems;
import Emazon.MicroServiceShopCart.domain.models.Item;
import Emazon.MicroServiceShopCart.domain.pagination.PageCustom;
import Emazon.MicroServiceShopCart.domain.pagination.PageRequestCustom;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class CartHandler implements ICartHandler{

    private final IShoppingCartServicePort shoppingCarServicePort;
    private final ProductRequestMapper productRequestMapper;
    private final IShoppingCartServicePort shoppingCartServicePort;
    private final ShoppingCartItemResponseMapper shoppingCartItemResponseMapper;


    @Override
    public void addProductToCart(ItemRequest itemRequest, Long userId) {
        Item item = productRequestMapper.toItem(itemRequest);
        shoppingCarServicePort.addProduct(item, userId);
    }

    @Override
    public void removeProduct(Long productId, Long userId) {
        shoppingCarServicePort.removeProduct(productId, userId);
    }

    @Override
    public PageCustom<ShoppingCartItemResponse> getCartItems(Long userId, PageRequestCustom pageRequest, String categoryName, String brandName) {
        PageCustom<CartItems> cartItemsPage = shoppingCartServicePort.getPaginatedCartItems(userId, pageRequest, categoryName, brandName);
        return new PageCustom<>(
                shoppingCartItemResponseMapper.toResponseList(cartItemsPage.getContent()),
                cartItemsPage.getTotalElements(),
                cartItemsPage.getTotalPages(),
                cartItemsPage.getCurrentPage(),
                cartItemsPage.isAscending(),
                cartItemsPage.getTotalPrice()
        );
    }

}