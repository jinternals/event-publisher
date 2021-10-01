package com.jinternals.product.controllers;

import com.jinternals.product.domain.Product;
import com.jinternals.product.services.IdentityGenerator;
import com.jinternals.product.services.ProductService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class ProductController {
    private ProductService productService;
    private IdentityGenerator identityGenerator;

    public ProductController(ProductService productService, IdentityGenerator identityGenerator) {
        this.productService = productService;
        this.identityGenerator = identityGenerator;
    }

    @GetMapping("/product/{productId}")
    public Mono<Product> getProduct(@PathVariable("productId") String productId){
        return productService.getProduct(productId);
    }

    @PostMapping("/product")
    public Mono<Product> getProduct(@RequestBody Product product){
        product.setId(identityGenerator.generateIdentity());
        return productService.saveProduct(product);
    }
}
