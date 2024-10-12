package Emazon.MicroServiceShopCart.infrastructure.input.rest;

import Emazon.MicroServiceShopCart.application.dto.request.ItemRequest;
import Emazon.MicroServiceShopCart.application.dto.response.ShoppingCartItemResponse;
import Emazon.MicroServiceShopCart.application.handler.ICartHandler;
import Emazon.MicroServiceShopCart.domain.pagination.PageCustom;
import Emazon.MicroServiceShopCart.domain.pagination.PageRequestCustom;
import Emazon.MicroServiceShopCart.infrastructure.security.SecurityUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/shopping-cart")
@RequiredArgsConstructor

public class ShoppingCartRestController {

    private final ICartHandler cartHandler;

    @Operation(summary = "Añadir un producto al carrito de compras",
            description = "Añade un producto al carrito de compras de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto añadido al carrito"),
            @ApiResponse(responseCode = "400", description = "Petición inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/add-products")
    public ResponseEntity<Void> addProductToShoppingCart(@Valid @RequestBody ItemRequest itemRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
        Long userId = userDetails.getId();
        cartHandler.addProductToCart(itemRequest, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Eliminar un producto del carrito de compras",
            description = "Elimina un producto del carrito de compras de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado del carrito"),
            @ApiResponse(responseCode = "400", description = "Petición inválida"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado en el carrito"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/remove-product/{productId}")
    public ResponseEntity<Void> removeProductFromShoppingCart(@PathVariable Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
        Long userId = userDetails.getId();

        cartHandler.removeProduct(productId, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener productos del carrito paginados con filtros",
            description = "Obtiene los productos del carrito de compras paginados, con la posibilidad de filtrar por categoría o marca, y ordenar ascendentemente o descendentemente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos del carrito obtenidos exitosamente"),
            @ApiResponse(responseCode = "400", description = "Petición inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/items")
    public ResponseEntity<PageCustom<ShoppingCartItemResponse>> getPaginatedCartItems(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "true") boolean ascending,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String brandName
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
        Long userId = userDetails.getId();
        PageRequestCustom pageRequest = new PageRequestCustom(page, size, sortField, ascending);
        PageCustom<ShoppingCartItemResponse> cartItems = cartHandler.getCartItems(userId, pageRequest, categoryName, brandName);
        return ResponseEntity.ok(cartItems);
    }
}
