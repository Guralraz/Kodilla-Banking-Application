package com.greg.banking_app.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountNumberGeneratorTest {

    @Autowired
    private AccountNumberGenerator accountNumberGenerator;

    @Test
    void shouldGenerateAccountNumber() {
        //Given
        Long id1 = 1L;
        Long id2 = 32L;
        Long id3 = 1234L;
        Long id4 = 1000000000000001234L;
        //When
        String number1 = accountNumberGenerator.generateStandardNumber(id1);
        String number2 = accountNumberGenerator.generateStandardNumber(id2);
        String number3 = accountNumberGenerator.generateStandardNumber(id3);
        String number4 = accountNumberGenerator.generateStandardNumber(id4);
        //Then
        assertEquals("12345678900000000000000001", number1);
        assertEquals("12345678900000000000000032", number2);
        assertEquals("12345678900000000000001234", number3);
        assertEquals("12345679900000000000001234", number4);
    }

}