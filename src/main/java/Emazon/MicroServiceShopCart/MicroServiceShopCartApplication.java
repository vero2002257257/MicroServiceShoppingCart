package Emazon.MicroServiceShopCart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MicroServiceShopCartApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroServiceShopCartApplication.class, args);
	}

}
