package Emazon.MicroServiceShopCart.infrastructure.jpa.repository;

import Emazon.MicroServiceShopCart.infrastructure.jpa.entity.ShoppingCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IShoppingCartRepository extends JpaRepository<ShoppingCartEntity, Long> {
    boolean existsByUserId(Long userId);
    Optional<ShoppingCartEntity> findByUserId(Long userId);
}
