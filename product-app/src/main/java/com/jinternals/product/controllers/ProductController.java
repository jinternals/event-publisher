package com.jinternals.product.controllers;

import com.jinternals.product.domain.Product;
import com.jinternals.product.services.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ProductController {
    private ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product/{productId}")
    public Mono<Product> getProduct(@PathVariable("productId") String productId){
        return productService.getProduct(productId);
    }
}
