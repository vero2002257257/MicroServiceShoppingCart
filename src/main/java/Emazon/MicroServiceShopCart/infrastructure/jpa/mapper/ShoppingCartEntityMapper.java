package Emazon.MicroServiceShopCart.infrastructure.jpa.mapper;

import Emazon.MicroServiceShopCart.domain.models.ShoppingCart;
import Emazon.MicroServiceShopCart.infrastructure.jpa.entity.ShoppingCartEntity;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ShoppingCartEntityMapper {

    ShoppingCart toShoppingCar(ShoppingCartEntity shoppingCartEntity);
    @Mapping(target = "userId", source = "userId")
    ShoppingCartEntity toEntity(ShoppingCart shoppingCart);

}