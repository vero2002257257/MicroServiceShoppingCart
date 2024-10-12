package Emazon.MicroServiceShopCart.infrastructure.jpa.repository;

import Emazon.MicroServiceShopCart.infrastructure.jpa.entity.ItemCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ICartItemRepository extends JpaRepository<ItemCartEntity, Long> {

    List<ItemCartEntity> findAllByShoppingCartEntity_Id(Long shoppingCartId);
}