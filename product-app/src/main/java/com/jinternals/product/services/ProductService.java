package com.jinternals.product.services;

import com.couchbase.transactions.TransactionResult;
import com.couchbase.transactions.Transactions;
import com.jinternals.product.domain.Product;
import com.jinternals.product.repositories.ProductRepository;
import org.springframework.data.couchbase.CouchbaseClientFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class ProductService {

    private Transactions transactions;
    private CouchbaseClientFactory couchbaseClientFactory;
    private ProductRepository productRepository;

    public ProductService(Transactions transactions,
                          CouchbaseClientFactory couchbaseClientFactory,
                          ProductRepository productRepository) {
        this.transactions = transactions;
        this.couchbaseClientFactory = couchbaseClientFactory;
        this.productRepository = productRepository;
    }


    public Mono<Product> saveProduct(Product product){
        Mono<TransactionResult> result = transactions.reactive().run((ctx) -> {
            // 'ctx' is an AttemptContextReactive, providing asynchronous versions of the
            return
                    ctx.insert(couchbaseClientFactory.getDefaultCollection().reactive(), product.getId(), product)
//                            .then(ctx.get(couchbaseClientFactory.getDefaultCollection().reactive(), product.getId()).flatMap(
//                                    doc -> {
//                                        Product content = doc.contentAs(Product.class);
//                                        return ctx.insert(couchbaseClientFactory.getDefaultCollection().reactive(),
//                                                content.getOrderId(), new OrderToPromiseLookup(
//                                                        content.getInflight().getOrderId(), content.getId()));
//                                    }
//                            ))
                            .then(ctx.commit());
        }).doOnError(throwable -> {
            String errorMessage = "Transaction failed";
           // log.error(errorMessage, PROMISE_ID.getField(promise.getId()));
            //return new PersistenceException(ErrorCode.DB_ERROR.getCode(), throwable, errorMessage);
        });
        return result.then(Mono.just(product));
    }

    public Mono<Product> getProduct(String productId) {
        return productRepository.findById(productId);
    }
}
