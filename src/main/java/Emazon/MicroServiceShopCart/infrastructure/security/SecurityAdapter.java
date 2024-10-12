package Emazon.MicroServiceShopCart.infrastructure.security;

import Emazon.MicroServiceShopCart.domain.spi.ISecurityPersistencePort;
import org.springframework.stereotype.Service;

@Service
public class SecurityAdapter implements ISecurityPersistencePort {
    private static final ThreadLocal<String> tokenHolder = new ThreadLocal<>();

    @Override
    public void setToken(String jwtToken) {
        tokenHolder.set(jwtToken);
    }

    @Override
    public String getToken() {
        return tokenHolder.get();
    }

    @Override
    public void removeToken(){
        tokenHolder.remove();
    }
}