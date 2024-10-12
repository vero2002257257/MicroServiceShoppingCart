package Emazon.MicroServiceShopCart.domain.spi;

import Emazon.MicroServiceShopCart.domain.models.Product;

import java.time.LocalDateTime;
import java.util.List;

public interface IProductPersistencePort {
    Product getProductById(Long id);
}
