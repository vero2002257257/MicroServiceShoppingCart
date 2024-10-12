package Emazon.MicroServiceShopCart.infrastructure.jpa.adapter;

import Emazon.MicroServiceShopCart.domain.spi.ITransactionsPersistencePort;
import Emazon.MicroServiceShopCart.infrastructure.feign.client.TransactionsFeignClient;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class TransactionsFeignJpaAdapter implements ITransactionsPersistencePort {

    private final TransactionsFeignClient transactionsFeignClient;

    @Override
    public LocalDateTime nextSupplyDate(Long productId) {
        return transactionsFeignClient.nextSupplyDate(productId);
    }
}