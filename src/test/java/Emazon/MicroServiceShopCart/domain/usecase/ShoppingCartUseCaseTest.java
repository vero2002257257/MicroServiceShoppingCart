package Emazon.MicroServiceShopCart.domain.usecase;

import Emazon.MicroServiceShopCart.domain.exception.InvalidQuantityProducts;
import Emazon.MicroServiceShopCart.domain.exception.OutOfStockException;
import Emazon.MicroServiceShopCart.domain.exception.ShoppingCartNotFound;
import Emazon.MicroServiceShopCart.domain.models.*;
import Emazon.MicroServiceShopCart.domain.pagination.PageCustom;
import Emazon.MicroServiceShopCart.domain.pagination.PageRequestCustom;
import Emazon.MicroServiceShopCart.domain.spi.IProductPersistencePort;
import Emazon.MicroServiceShopCart.domain.spi.IShoppingCartPersistencePort;
import Emazon.MicroServiceShopCart.domain.spi.ITransactionsPersistencePort;
import Emazon.MicroServiceShopCart.domain.spi.ItemPersistencePort;
import Emazon.MicroServiceShopCart.infrastructure.exception.ItemNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class ShoppingCartUseCaseTest {

    @Mock
    private IShoppingCartPersistencePort shoppingCartPersistencePort;

    @Mock
    private IProductPersistencePort productPersistencePort;

    @Mock
    private ItemPersistencePort itemPersistencePort;

    @Mock
    private ITransactionsPersistencePort transactionsPersistencePort;

    @InjectMocks
    private ShoppingCartUseCase shoppingCartUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddNewProductToCart() {
        // Arrange
        Long userId = 1L;
        Item item = new Item();
        item.setProductId(1L);
        item.setQuantity(1);

        Product product = new Product();
        product.setId(1L);
        product.setQuantity(10);
        product.setCategories(new ArrayList<>());

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setItems(new ArrayList<>());

        when(shoppingCartPersistencePort.getShoppingCartByUserId(userId)).thenReturn(Optional.of(shoppingCart));
        when(productPersistencePort.getProductById(1L)).thenReturn(product);
        when(itemPersistencePort.save(any(Item.class))).thenReturn(item);

        // Act
        shoppingCartUseCase.addProduct(item, userId);

        // Assert
        verify(itemPersistencePort, times(1)).save(any(Item.class));
        verify(shoppingCartPersistencePort, times(1)).updateCart(any(ShoppingCart.class));
    }

    @Test
    void shouldUpdateExistingProductQuantityInCart() {
        // Arrange
        Long userId = 1L;
        Item existingItem = new Item();
        existingItem.setProductId(1L);
        existingItem.setQuantity(1);

        Item newItem = new Item();
        newItem.setProductId(1L);
        newItem.setQuantity(3);

        Product product = new Product();
        product.setId(1L);
        product.setQuantity(10);
        product.setCategories(Collections.emptyList());

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setItems(Collections.singletonList(existingItem));

        when(shoppingCartPersistencePort.getShoppingCartByUserId(userId)).thenReturn(Optional.of(shoppingCart));
        when(productPersistencePort.getProductById(1L)).thenReturn(product);
        when(itemPersistencePort.save(existingItem)).thenReturn(existingItem);

        // Act
        shoppingCartUseCase.addProduct(newItem, userId);

        // Assert
        assertEquals(4, existingItem.getQuantity());
        verify(itemPersistencePort, times(1)).save(existingItem);
        verify(shoppingCartPersistencePort, times(1)).updateCart(any(ShoppingCart.class));
    }

    @Test
    void shouldThrowOutOfStockExceptionWhenProductIsOutOfStock() {
        // Arrange
        Long userId = 1L;
        Item item = new Item();
        item.setProductId(1L);
        item.setQuantity(10);

        Product product = new Product();
        product.setId(1L);
        product.setQuantity(5); // Stock insuficiente

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setItems(Collections.emptyList());

        when(shoppingCartPersistencePort.getShoppingCartByUserId(userId)).thenReturn(Optional.of(shoppingCart));
        when(productPersistencePort.getProductById(1L)).thenReturn(product);

        // Act & Assert
        OutOfStockException exception = assertThrows(OutOfStockException.class, () -> shoppingCartUseCase.addProduct(item, userId));

        assertTrue(exception.getMessage().contains("No hay suficiente stock del producto"));
        verify(itemPersistencePort, never()).save(any(Item.class)); // No debería guardar el ítem
    }

    @Test
    void shouldThrowInvalidQuantityProductsWhenCategoryLimitExceeded() {
        // Arrange
        Long userId = 1L;

        // Crear nuevo ítem a agregar con la misma categoría "Electronics"
        Item newItem = new Item();
        newItem.setProductId(1L); // ID del producto
        newItem.setQuantity(1);

        // Producto que se intenta agregar con categoría "Electronics"
        Product product = new Product();
        product.setId(1L);
        product.setCategories(Collections.singletonList(new Category(1L, "Electronics", "description")));

        // Crear ítems existentes en el carrito
        Item existingItem1 = new Item();
        existingItem1.setProductId(2L);
        existingItem1.setQuantity(1);
        Product product1 = new Product();
        product1.setId(2L);
        product1.setCategories(Collections.singletonList(new Category(1L, "Electronics", "description")));

        Item existingItem2 = new Item();
        existingItem2.setProductId(3L);
        existingItem2.setQuantity(1);
        Product product2 = new Product();
        product2.setId(3L);
        product2.setCategories(Collections.singletonList(new Category(1L, "Electronics", "description")));

        Item existingItem3 = new Item();
        existingItem3.setProductId(4L);
        existingItem3.setQuantity(1);
        Product product3 = new Product();
        product3.setId(4L);
        product3.setCategories(Collections.singletonList(new Category(1L, "Electronics", "description")));

        // Crear carrito de compras con los ítems existentes
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setItems(Arrays.asList(existingItem1, existingItem2, existingItem3));

        // Mock configurations
        when(shoppingCartPersistencePort.getShoppingCartByUserId(userId)).thenReturn(Optional.of(shoppingCart));
        when(productPersistencePort.getProductById(1L)).thenReturn(product);
        when(productPersistencePort.getProductById(2L)).thenReturn(product1);
        when(productPersistencePort.getProductById(3L)).thenReturn(product2);
        when(productPersistencePort.getProductById(4L)).thenReturn(product3);

        // Act & Assert
        InvalidQuantityProducts exception = assertThrows(InvalidQuantityProducts.class,
                () -> shoppingCartUseCase.addProduct(newItem, userId));

        // Verificar mensaje de excepción
        assertEquals("Solo puedes agregar 3 productos por categoria", exception.getMessage());

        // Verificar que los mocks fueron llamados
        verify(shoppingCartPersistencePort).getShoppingCartByUserId(userId);
        verify(productPersistencePort).getProductById(1L);
        verify(productPersistencePort).getProductById(2L);
        verify(productPersistencePort).getProductById(3L);
        verify(productPersistencePort).getProductById(4L);
    }

    @Test
    void removeProduct_shouldRemoveProductFromCartSuccessfully() {
        // Arrange
        Long userId = 1L;
        Long productId = 1L;

        Item item1 = new Item();
        item1.setProductId(1L);

        Item item2 = new Item();
        item2.setProductId(2L);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setItems(new ArrayList<>(List.of(item1, item2)));

        when(shoppingCartPersistencePort.getShoppingCartByUserId(userId))
                .thenReturn(Optional.of(shoppingCart));

        // Act
        shoppingCartUseCase.removeProduct(productId, userId);

        // Assert
        assertEquals(1, shoppingCart.getItems().size());
        verify(shoppingCartPersistencePort, times(1)).updateCart(shoppingCart);
    }

    @Test
    void removeProduct_shouldThrowItemNotFoundExceptionIfProductNotInCart() {
        // Arrange
        Long userId = 1L;
        Long productId = 3L; // Product not in cart

        Item item1 = new Item();
        item1.setProductId(1L);

        Item item2 = new Item();
        item2.setProductId(2L);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setItems(new ArrayList<>(List.of(item1, item2)));

        when(shoppingCartPersistencePort.getShoppingCartByUserId(userId))
                .thenReturn(Optional.of(shoppingCart));

        // Act & Assert
        assertThrows(ItemNotFoundException.class, () -> shoppingCartUseCase.removeProduct(productId, userId));
        verify(shoppingCartPersistencePort, never()).updateCart(shoppingCart);  // No se debería llamar a updateCart
    }

    @Test
    void removeProduct_shouldThrowShoppingCartNotFoundExceptionIfCartDoesNotExist() {
        // Arrange
        Long userId = 1L;
        Long productId = 1L;

        when(shoppingCartPersistencePort.getShoppingCartByUserId(userId))
                .thenReturn(Optional.empty()); // Carrito no encontrado

        // Act & Assert
        assertThrows(ShoppingCartNotFound.class, () -> shoppingCartUseCase.removeProduct(productId, userId));
        verify(shoppingCartPersistencePort, never()).updateCart(any());  // No se debería llamar a updateCart
    }


    @Test
    void getPaginatedCartItems_ShouldReturnCorrectPage() {
        Long userId = 1L;
        Item item1 = new Item(1L, 1L, 2, new ShoppingCart());
        Item item2 = new Item(2L, 2L, 1, new ShoppingCart());
        Product product1 = new Product(1L, "Product1", "Description1", 10, 100.0, null, Collections.emptyList());
        Product product2 = new Product(2L, "Product2", "Description2", 0, 200.0, null, Collections.emptyList());

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setItems(Arrays.asList(item1, item2));

        when(shoppingCartPersistencePort.getShoppingCartByUserId(userId)).thenReturn(Optional.of(shoppingCart));
        when(productPersistencePort.getProductById(1L)).thenReturn(product1);
        when(productPersistencePort.getProductById(2L)).thenReturn(product2);
        when(transactionsPersistencePort.nextSupplyDate(2L)).thenReturn(LocalDateTime.now().plusDays(30));

        PageRequestCustom pageRequest = new PageRequestCustom(0, 10, "name", true);

        PageCustom<CartItems> result = shoppingCartUseCase.getPaginatedCartItems(1L, pageRequest, null, null);

        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(400.0, result.getTotalPrice());

        verify(shoppingCartPersistencePort, times(1)).getShoppingCartByUserId(1L);
        verify(productPersistencePort, times(4)).getProductById(anyLong());
    }

    @Test
    void getPaginatedCartItems_ShouldThrowExceptionWhenCartIsEmpty() {
        Long userId = 1L;
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setItems(Collections.emptyList());

        when(shoppingCartPersistencePort.getShoppingCartByUserId(userId)).thenReturn(Optional.of(shoppingCart));

        PageRequestCustom pageRequest = new PageRequestCustom(0, 10,  "name", true);

        assertThrows(OutOfStockException.class, () -> shoppingCartUseCase.getPaginatedCartItems(1L, pageRequest, null, null));
    }

    @Test
    void getPaginatedCartItems_ShouldReturnPaginatedResult() {
        Long userId = 1L;
        Item item1 = new Item(1L, 1L, 2, new ShoppingCart());
        Item item2 = new Item(2L, 2L, 1, new ShoppingCart());
        Product product1 = new Product(1L, "Product1", "Description1", 10, 100.0, null, Collections.emptyList());
        Product product2 = new Product(2L, "Product2", "Description2", 0, 200.0, null, Collections.emptyList());

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setItems(Arrays.asList(item1, item2));

        when(shoppingCartPersistencePort.getShoppingCartByUserId(userId)).thenReturn(Optional.of(shoppingCart));
        when(productPersistencePort.getProductById(1L)).thenReturn(product1);
        when(productPersistencePort.getProductById(2L)).thenReturn(product2);
        when(transactionsPersistencePort.nextSupplyDate(anyLong())).thenReturn(LocalDateTime.now());

        PageRequestCustom pageRequest = new PageRequestCustom(0, 1, "name", true);

        PageCustom<CartItems> result = shoppingCartUseCase.getPaginatedCartItems(1L, pageRequest, null, null);

        assertEquals(1, result.getContent().size());
        assertEquals(2, result.getTotalElements());
    }

}