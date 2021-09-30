package com.jinternals.product.configuration;

import com.couchbase.client.java.Cluster;
import com.couchbase.transactions.TransactionDurabilityLevel;
import com.couchbase.transactions.Transactions;
import com.couchbase.transactions.config.TransactionConfigBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TransactionManagerConfig {

  @Bean
  public Transactions transactions(final Cluster couchbaseCluster) {
    return Transactions.create(couchbaseCluster, TransactionConfigBuilder.create()
                    .durabilityLevel(TransactionDurabilityLevel.NONE)
      .build());
  }
}
