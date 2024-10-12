package Emazon.MicroServiceShopCart.infrastructure.feign.configuration;

import Emazon.MicroServiceShopCart.infrastructure.security.SecurityAdapter;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static Emazon.MicroServiceShopCart.utils.Constants.AUTH_TOKEN;

@Component
@RequiredArgsConstructor
public class FeignClientInterceptor implements RequestInterceptor {

    private final SecurityAdapter securityAdapter;

    @Override
    public void apply(RequestTemplate template) {
        String token = securityAdapter.getToken();

        if (token != null && !token.isEmpty()) {
            template.header(AUTH_TOKEN, token);
        }
    }
}