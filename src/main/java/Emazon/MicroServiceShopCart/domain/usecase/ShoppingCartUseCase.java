package Emazon.MicroServiceShopCart.domain.usecase;


import Emazon.MicroServiceShopCart.domain.api.IShoppingCartServicePort;
import Emazon.MicroServiceShopCart.domain.exception.InvalidQuantityProducts;
import Emazon.MicroServiceShopCart.domain.exception.OutOfStockException;
import Emazon.MicroServiceShopCart.domain.models.Item;
import Emazon.MicroServiceShopCart.domain.models.Product;
import Emazon.MicroServiceShopCart.domain.models.ShoppingCart;
import Emazon.MicroServiceShopCart.domain.spi.IProductPersistencePort;
import Emazon.MicroServiceShopCart.domain.spi.IShoppingCartPersistencePort;
import Emazon.MicroServiceShopCart.domain.spi.ItemPersistencePort;

import java.time.LocalDateTime;
import java.util.Optional;

import static Emazon.MicroServiceShopCart.utils.Constants.INVALID_QUANTITY_PRODUCTS;
import static Emazon.MicroServiceShopCart.utils.Constants.OUT_OF_STOCK;


public class ShoppingCartUseCase implements IShoppingCartServicePort {

    private final IShoppingCartPersistencePort shoppingCartPersistencePort;
    private final IProductPersistencePort productPersistencePort;
    private final ItemPersistencePort itemPersistencePort;

    public ShoppingCartUseCase(IShoppingCartPersistencePort shoppingCarPersistencePort, IProductPersistencePort productPersistencePort, ItemPersistencePort itemPersistencePort) {
        this.shoppingCartPersistencePort = shoppingCarPersistencePort;
        this.productPersistencePort = productPersistencePort;
        this.itemPersistencePort = itemPersistencePort;
    }

    @Override
    public void addProduct(Item item, Long userId) {
        ShoppingCart shoppingCart = getOrCreateShoppingCart(userId);
        item.setShoppingCart(shoppingCart);
        Product product = getProduct(item.getProductId());

        Optional<Item> existingItem = findExistingItem(shoppingCart, item.getProductId());

        existingItem.ifPresentOrElse(
                itemInCart -> {
                    int totalQuantityInCart = itemInCart.getQuantity() + item.getQuantity();
                    itemInCart.setShoppingCart(shoppingCart);
                    validateStock(product, totalQuantityInCart);
                    updateExistingItem(itemInCart, item.getQuantity());
                },
                () -> {
                    validateCategoryLimit(shoppingCart, product);
                    validateStock(product, item.getQuantity());
                    addNewItemToCart(shoppingCart, item);
                }
        );

        updateShoppingCart(shoppingCart);
    }

    private ShoppingCart getOrCreateShoppingCart(Long userId) {
        return shoppingCartPersistencePort.getShoppingCartByUserId(userId)
                .orElseGet(() -> shoppingCartPersistencePort.createShoppingCart(userId));
    }

    private Product getProduct(Long productId) {
        return productPersistencePort.getProductById(productId);
    }

    private void validateStock(Product product, int requestedQuantity) {
        if (product.getQuantity() < requestedQuantity) {
            LocalDateTime replenishmentDate = LocalDateTime.now().plusMonths(1);
            throw new OutOfStockException(OUT_OF_STOCK + replenishmentDate);
        }
    }

    private void validateCategoryLimit(ShoppingCart shoppingCart, Product product) {
        long categoryCount = countItemsInSameCategory(shoppingCart, product);
        if (categoryCount >= 3) {
            throw new InvalidQuantityProducts(INVALID_QUANTITY_PRODUCTS);
        }
    }

    private long countItemsInSameCategory(ShoppingCart shoppingCart, Product product) {
        // Contar cuántos productos en el carrito tienen categorías en común con el producto actual
        return shoppingCart.getItems().stream()
                .filter(i -> {
                    // Obtener el producto del ítem actual
                    Product itemProduct = productPersistencePort.getProductById(i.getProductId());
                    // Comparar las categorías del producto en el carrito con las del nuevo producto
                    return itemProduct.getCategories().stream()
                            .anyMatch(category -> product.getCategories().stream()
                                    .anyMatch(c -> c.getId().equals(category.getId())));
                })
                .count();
    }


    private Optional<Item> findExistingItem(ShoppingCart shoppingCart, Long productId) {
        return shoppingCart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst();
    }

    private void updateExistingItem(Item existingItem, int additionalQuantity) {
        existingItem.setQuantity(existingItem.getQuantity() + additionalQuantity);
        itemPersistencePort.save(existingItem);
    }

    private void addNewItemToCart(ShoppingCart shoppingCart, Item newItem) {
        newItem.setShoppingCart(shoppingCart);
        Item savedItem = itemPersistencePort.save(newItem);
        shoppingCart.getItems().add(savedItem);
    }

    private void updateShoppingCart(ShoppingCart shoppingCart) {
        shoppingCart.setActualizationDate(LocalDateTime.now());
        shoppingCartPersistencePort.updateCart(shoppingCart);
    }
}
