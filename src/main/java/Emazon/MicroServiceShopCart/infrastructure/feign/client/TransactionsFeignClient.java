package Emazon.MicroServiceShopCart.infrastructure.feign.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;

import static Emazon.MicroServiceShopCart.utils.Constants.TRANSACTIONS_FEIGN_NAME;
import static Emazon.MicroServiceShopCart.utils.Constants.URL_TRANSACTIONS_FEIGN_CLIENT;


@FeignClient(name = TRANSACTIONS_FEIGN_NAME, url = URL_TRANSACTIONS_FEIGN_CLIENT)
public interface TransactionsFeignClient {

    @GetMapping("/supply/last-supply/{productId}")
    LocalDateTime  nextSupplyDate(@PathVariable Long productId);
}
