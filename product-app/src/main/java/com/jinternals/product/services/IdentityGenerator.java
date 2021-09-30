package com.jinternals.product.services;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IdentityGenerator {

    public String generateIdentity(){
        return UUID.randomUUID().toString();
    }
}
