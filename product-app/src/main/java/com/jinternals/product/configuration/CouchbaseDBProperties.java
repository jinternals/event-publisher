package com.jinternals.product.configuration;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "services.couchbase")
public class CouchbaseDBProperties {

    private CouchbaseProperties event = new CouchbaseProperties();

    public CouchbaseProperties getEvent() {
        return event;
    }

    public void setEvent(CouchbaseProperties event) {
        this.event = event;
    }
}
