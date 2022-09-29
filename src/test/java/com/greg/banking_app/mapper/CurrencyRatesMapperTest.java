package com.greg.banking_app.mapper;

import com.greg.banking_app.domain.CurrencyRates;
import com.greg.banking_app.dto.NBPClient.NBPCurrencyDto;
import com.greg.banking_app.dto.NBPClient.NBPTableCDto;
import com.greg.banking_app.dto.currency.CurrencyDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CurrencyRatesMapperTest {

    @Autowired
    private CurrencyRatesMapper mapper;

    @Test
    void mapToCurrencyValueTest() {
        //Given
        NBPCurrencyDto currencyDto1 =
                new NBPCurrencyDto("USD", new BigDecimal("4.4321"), new BigDecimal("4.5432"));
        NBPCurrencyDto currencyDto2 =
                new NBPCurrencyDto("EUR", new BigDecimal("5.4321"), new BigDecimal("5.5432"));
        NBPCurrencyDto currencyDto3 =
                new NBPCurrencyDto("CHF", new BigDecimal("6.4321"), new BigDecimal("6.5432"));
        List<NBPCurrencyDto> rates = List.of(currencyDto1, currencyDto2, currencyDto3);
        NBPTableCDto dto = new NBPTableCDto("C", "1111/C/Test", "TestDate", rates);
        //When
        List<CurrencyRates> list = mapper.mapToCurrencyValue(dto);
        //Then
        assertEquals(3, list.size());
        assertEquals("1111/C/Test", list.get(0).getTableNo());
        assertEquals(new BigDecimal("5.4321"), list.get(1).getBuyRate());
        assertEquals(new BigDecimal("6.5432"), list.get(2).getSellRate());
        assertNotEquals("USD", list.get(1).getCurrencyIsoCode());
        assertEquals("CHF", list.get(2).getCurrencyIsoCode());
    }

    @Test
    void mapToCurrencyDtoTest() {
        //Given
        CurrencyRates currencyRates = getCurrencyRatesList().get(0);
        //When
        CurrencyDto dto = mapper.mapToCurrencyDto(currencyRates);
        //Then
        assertEquals("TestTable", dto.getTableNo());
        assertEquals(new BigDecimal("4.5555"), dto.getBuyRate());
        assertNotEquals(new BigDecimal("4.5555"), dto.getSellRate());
    }

    @Test
    void mapToCurrencyDtoList() {
        //Given
        List<CurrencyRates> currencyRatesList = getCurrencyRatesList();
        //When
        List<CurrencyDto> dtos = mapper.mapToCurrencyDtoList(currencyRatesList);
        List<CurrencyDto> usdDto = dtos.stream()
                .filter(currencyDto -> currencyDto.getCurrencyIsoCode().equals("USD"))
                .collect(Collectors.toList());
        //Then
        assertEquals(3, dtos.size());
        assertEquals(1, usdDto.size());
        assertEquals("TestTable", dtos.get(1).getTableNo());
        assertEquals(new BigDecimal("4.5555"), usdDto.get(0).getBuyRate());
        assertNotEquals(new BigDecimal("4.5555"), dtos.get(2).getSellRate());
        assertNotEquals(new BigDecimal("6.5555"), dtos.get(1).getSellRate());
    }

    private List<CurrencyRates> getCurrencyRatesList() {
        return List.of(
                new CurrencyRates(1L, "C", "TestTable", "2022-07-10",
                        "USD", new BigDecimal("4.5555"), new BigDecimal("4.6666")),
                new CurrencyRates(2L, "C", "TestTable", "2022-07-10",
                        "CHF", new BigDecimal("5.5555"), new BigDecimal("5.6666")),
                new CurrencyRates(3L, "C", "TestTable", "2022-07-10",
                        "EUR", new BigDecimal("5.0000"), new BigDecimal("5.2000"))
        );
    }
}