package com.jinternals.product.repositories.product;

import com.jinternals.product.domain.Product;
import org.springframework.data.couchbase.repository.ReactiveCouchbaseRepository;

public interface ProductRepository extends ReactiveCouchbaseRepository<Product, String> {
}
