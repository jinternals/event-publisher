package com.jinternals.product.services;

import com.couchbase.transactions.TransactionResult;
import com.couchbase.transactions.Transactions;
import com.jinternals.product.configuration.couchbase.EventCouchbaseConfiguration;
import com.jinternals.product.domain.Product;
import com.jinternals.product.domain.ProductCreatedEvent;
import com.jinternals.product.repositories.product.ProductRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.couchbase.CouchbaseClientFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class ProductService {

    private Transactions transactions;
    private CouchbaseClientFactory couchbaseClientFactory;
    private CouchbaseClientFactory eventCouchbaseClientFactory;
    private ProductRepository productRepository;
    private IdentityGenerator identityGenerator;
    public ProductService(Transactions transactions,
                          CouchbaseClientFactory couchbaseClientFactory,
                          @Qualifier(value = EventCouchbaseConfiguration.COUCHBASE_CLIENT_FACTORY)
                                  CouchbaseClientFactory eventCouchbaseClientFactory,
                          ProductRepository productRepository, IdentityGenerator identityGenerator) {
        this.transactions = transactions;
        this.couchbaseClientFactory = couchbaseClientFactory;
        this.eventCouchbaseClientFactory = eventCouchbaseClientFactory;
        this.productRepository = productRepository;
        this.identityGenerator = identityGenerator;
    }


    public Mono<Product> saveProduct(Product product){

        ProductCreatedEvent event = new ProductCreatedEvent();
        event.setProduct(product);
        event.setId(identityGenerator.generateIdentity());

        Mono<TransactionResult> result = transactions.reactive().run((ctx) -> {
            // 'ctx' is an AttemptContextReactive, providing asynchronous versions of the
            return
                            ctx.insert(couchbaseClientFactory.getDefaultCollection().reactive(), product.getId(), product)
                            .then( ctx.insert(eventCouchbaseClientFactory.getDefaultCollection().reactive(), event.getId(), event))
                             .flatMap(transactionGetResult -> Mono.error(new RuntimeException()))
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
