package com.greg.banking_app.client;

import com.greg.banking_app.dto.NBPClient.NBPCurrencyDto;
import com.greg.banking_app.dto.NBPClient.NBPTableCDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NBPClientTest {

    @Autowired
    private NBPClient nbpClient;

    @Test
    void shouldGetTableC() {
        //Given
        //When
        List<NBPTableCDto> list = nbpClient.getNBPTableC();
        List<NBPCurrencyDto> currencies = list.get(0).getRates();
        List<String> currencyCodes = currencies.stream().map(NBPCurrencyDto::getCurrencyCode).collect(Collectors.toList());
        List<String> expectedList = Arrays.asList("USD", "CHF", "EUR");
        //Then
        assertEquals(1, list.size());
        assertEquals("C", list.get(0).getTable());
        assertTrue(currencyCodes.containsAll(expectedList));
    }
}