package com.jinternals.product.configuration.couchbase;


import com.jinternals.product.configuration.CouchbaseDBProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.SimpleCouchbaseClientFactory;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.core.ReactiveCouchbaseTemplate;
import org.springframework.data.couchbase.core.convert.MappingCouchbaseConverter;
import org.springframework.data.couchbase.repository.config.EnableReactiveCouchbaseRepositories;

import static com.couchbase.client.core.env.PasswordAuthenticator.create;

@Configuration
public class EventCouchbaseConfiguration {
    public static final String COUCHBASE_TEMPLATE = "eventCouchbaseTemplate";
    public static final String COUCHBASE_CLIENT_FACTORY = "eventCouchbaseClientFactory";
    public static final String BUCKET_NAME = "events";

    @Autowired
    private MappingCouchbaseConverter mappingCouchbaseConverter;


    @Bean(name = EventCouchbaseConfiguration.COUCHBASE_TEMPLATE)
    public ReactiveCouchbaseTemplate eventReactiveCouchbaseTemplate(@Qualifier(value = EventCouchbaseConfiguration.COUCHBASE_CLIENT_FACTORY)
                                                                    SimpleCouchbaseClientFactory couchbaseClientFactory) {
        return new ReactiveCouchbaseTemplate(couchbaseClientFactory, mappingCouchbaseConverter);
    }


    @Bean(name = EventCouchbaseConfiguration.COUCHBASE_CLIENT_FACTORY)
    public SimpleCouchbaseClientFactory eventCouchbaseClientFactory(CouchbaseDBProperties couchbaseDBProperties) {
        CouchbaseProperties event = couchbaseDBProperties.getEvent();
        return new SimpleCouchbaseClientFactory(event.getConnectionString(),
                create(event.getUsername(), event.getPassword()),
                EventCouchbaseConfiguration.BUCKET_NAME);
    }

}
