package Emazon.MicroServiceShopCart.infrastructure.configuration;

import Emazon.MicroServiceShopCart.domain.api.IShoppingCartServicePort;
import Emazon.MicroServiceShopCart.domain.spi.IProductPersistencePort;
import Emazon.MicroServiceShopCart.domain.spi.ISecurityPersistencePort;
import Emazon.MicroServiceShopCart.domain.spi.IShoppingCartPersistencePort;
import Emazon.MicroServiceShopCart.domain.spi.ItemPersistencePort;
import Emazon.MicroServiceShopCart.domain.usecase.ShoppingCartUseCase;
import Emazon.MicroServiceShopCart.infrastructure.feign.client.StockFeignClient;
import Emazon.MicroServiceShopCart.infrastructure.jpa.adapter.ItemJpaAdapter;
import Emazon.MicroServiceShopCart.infrastructure.jpa.adapter.ShoppingCartJpaAdapter;
import Emazon.MicroServiceShopCart.infrastructure.jpa.adapter.StockFeignJpaAdapter;
import Emazon.MicroServiceShopCart.infrastructure.jpa.mapper.ItemCartEntityMapper;
import Emazon.MicroServiceShopCart.infrastructure.jpa.mapper.ShoppingCartEntityMapper;
import Emazon.MicroServiceShopCart.infrastructure.jpa.repository.ICartItemRepository;
import Emazon.MicroServiceShopCart.infrastructure.jpa.repository.IShoppingCartRepository;
import Emazon.MicroServiceShopCart.infrastructure.security.SecurityAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import Emazon.MicroServiceShopCart.infrastructure.feign.client.StockFeignClient;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IShoppingCartRepository shoppingCarRepository;
    private final ShoppingCartEntityMapper shoppingCartEntityMapper;
    private final ItemCartEntityMapper itemCartEntityMapper;
    private final StockFeignClient stockFeignClient;
    private final ICartItemRepository cartItemRepository;

    @Bean
    public IShoppingCartPersistencePort shoppingCarPersistencePort() {
        return new ShoppingCartJpaAdapter(shoppingCarRepository, shoppingCartEntityMapper);
    }

    @Bean
    public IProductPersistencePort productPersistencePort() {
        return new StockFeignJpaAdapter(stockFeignClient);
    }

    @Bean
    public ItemPersistencePort itemPersistencePort() {
        return new ItemJpaAdapter(cartItemRepository, itemCartEntityMapper, shoppingCarRepository);
    }



    @Bean
    public StockFeignClient stockFeignClient() {
        return stockFeignClient;
    }

    @Bean
    public IShoppingCartServicePort shoppingCarServicePort(IShoppingCartPersistencePort shoppingCarPersistencePort, IProductPersistencePort productPersistencePort, ItemPersistencePort itemPersistencePort) {
        return new ShoppingCartUseCase(shoppingCarPersistencePort, productPersistencePort, itemPersistencePort);
    }

    @Bean
    public ISecurityPersistencePort securityPersistencePort() {
        return new SecurityAdapter();
    }
}