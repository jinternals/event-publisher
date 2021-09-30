package com.jinternals.product.configuration;

import com.jinternals.product.configuration.couchbase.EventCouchbaseConfiguration;
import com.jinternals.product.domain.ProductCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.ReactiveCouchbaseTemplate;
import org.springframework.data.couchbase.repository.config.EnableReactiveCouchbaseRepositories;
import org.springframework.data.couchbase.repository.config.ReactiveRepositoryOperationsMapping;


@Configuration
@EnableConfigurationProperties(CouchbaseDBProperties.class)
@EnableReactiveCouchbaseRepositories
public class CouchbaseConfiguration  extends AbstractCouchbaseConfiguration {

    @Autowired
    private CouchbaseProperties couchbaseProperties;

    @Value("${spring.couchbase.bucket}")
    private String bucket;

    @Qualifier(value = EventCouchbaseConfiguration.COUCHBASE_TEMPLATE)
    private ReactiveCouchbaseTemplate eventReactiveCouchbaseTemplate;
    @Override
    public String getConnectionString() {
        return couchbaseProperties.getConnectionString();
    }

    @Override
    public String getUserName() {
        return couchbaseProperties.getUsername();
    }

    @Override
    public String getPassword() {
        return couchbaseProperties.getPassword();
    }

  @Override
  public String getBucketName() {
    return this.bucket;
  }

    @Override
    protected void configureReactiveRepositoryOperationsMapping(ReactiveRepositoryOperationsMapping mapping) {
        mapping.mapEntity(ProductCreatedEvent.class, eventReactiveCouchbaseTemplate);
    }
}
