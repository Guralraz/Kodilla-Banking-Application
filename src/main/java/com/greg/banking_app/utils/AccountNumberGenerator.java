package com.greg.banking_app.utils;

import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class AccountNumberGenerator {

    public String generateStandardNumber(Long accountId) {
        BigInteger standardValue = new BigInteger("12345678900000000000000000");
        BigInteger accId = new BigInteger(accountId.toString());
        return standardValue.add(accId).toString();
    }
}
