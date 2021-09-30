package com.jinternals.product.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableReactiveCouchbaseRepositories;

@Configuration
@EnableReactiveCouchbaseRepositories
public class CouchbaseConfiguration extends AbstractCouchbaseConfiguration {

  @Autowired
  private CouchbaseProperties couchbaseProperties;

  @Value("${spring.couchbase.bucket}")
  private String bucket;

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


}
