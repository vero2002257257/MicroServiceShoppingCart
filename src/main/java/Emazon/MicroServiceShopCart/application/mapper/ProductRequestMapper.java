package Emazon.MicroServiceShopCart.application.mapper;

import Emazon.MicroServiceShopCart.application.dto.request.ItemRequest;
import Emazon.MicroServiceShopCart.domain.models.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProductRequestMapper {

    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "quantity", target = "quantity")
    Item toItem(ItemRequest itemRequest);
}