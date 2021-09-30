package com.jinternals.product;

import com.jinternals.product.domain.Product;
import com.jinternals.product.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class Application implements CommandLineRunner {
    @Autowired
    private ProductService productService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setName(UUID.randomUUID().toString());
        productService.saveProduct(product).subscribe();
    }
}
