package com.jinternals.product.services;

import com.couchbase.transactions.TransactionResult;
import com.couchbase.transactions.Transactions;
import com.jinternals.product.configuration.couchbase.EventCouchbaseConfiguration;
import com.jinternals.product.domain.Product;
import com.jinternals.product.domain.ProductCreatedEvent;
import com.jinternals.product.exceptions.PersistenceException;
import com.jinternals.product.repositories.product.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.couchbase.CouchbaseClientFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Slf4j
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

       return transactions.reactive().run((ctx) -> {
            return
                    ctx.insert(couchbaseClientFactory.getDefaultCollection().reactive(), product.getId(), product)
                    .then( ctx.insert(eventCouchbaseClientFactory.getDefaultCollection().reactive(), event.getId(), event))
//                    .then( ctx.get(eventCouchbaseClientFactory.getDefaultCollection().reactive(), event.getId()+1))
                    .then(ctx.commit());
        }).doOnError(throwable -> {
            log.error("Transaction failed", throwable );
            throw new PersistenceException("Transaction failed", throwable);
        }).then(Mono.just(product));

    }

    public Mono<Product> getProduct(String productId) {
        return productRepository.findById(productId);
    }
}
