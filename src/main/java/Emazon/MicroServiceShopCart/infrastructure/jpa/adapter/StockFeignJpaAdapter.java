package Emazon.MicroServiceShopCart.infrastructure.jpa.adapter;

import Emazon.MicroServiceShopCart.domain.models.Product;
import Emazon.MicroServiceShopCart.domain.spi.IProductPersistencePort;
import Emazon.MicroServiceShopCart.infrastructure.feign.client.StockFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
public class StockFeignJpaAdapter implements IProductPersistencePort {

    private final StockFeignClient stockFeignClient;


    @Override
    public Product getProductById(Long productId) {
        ResponseEntity<Product> response = stockFeignClient.getProductById(productId);
        return response.getBody();
    }
}