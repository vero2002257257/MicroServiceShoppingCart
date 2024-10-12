package Emazon.MicroServiceShopCart.infrastructure.jpa.mapper;

import Emazon.MicroServiceShopCart.domain.models.Item;
import Emazon.MicroServiceShopCart.infrastructure.jpa.entity.ItemCartEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ItemCartEntityMapper {

    @Mapping(target = "shoppingCart", ignore = true)
    Item toItem(ItemCartEntity itemCartEntity);

    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "quantity", target = "quantity")
    ItemCartEntity toItemCartEntity(Item item);

}