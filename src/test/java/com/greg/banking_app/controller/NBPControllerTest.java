package com.greg.banking_app.controller;

import com.greg.banking_app.client.NBPClient;
import com.greg.banking_app.dto.currency.CurrencyDto;
import com.greg.banking_app.mapper.CurrencyRatesMapper;
import com.greg.banking_app.service.CurrencyDbService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
@WebMvcTest(NBPController.class)
class NBPControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NBPClient client;

    @MockBean
    private CurrencyRatesMapper mapper;

    @MockBean
    private CurrencyDbService dbService;

    @Test
    void shouldGetTableC() throws Exception {
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/nbp/tableC")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldGetCurrencyRateFromDb() throws Exception {
        //Given
        CurrencyDto dto = getCurrencyDtoList().get(0);
        when(mapper.mapToCurrencyDto(dbService.getCurrencyRate(anyString()))).thenReturn(dto);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/nbp/database/currency/{isoCode}", "USD")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tableNo", Matchers.is("TestTable")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyIsoCode", Matchers.is("USD")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyRate", Matchers.is(4.5555)));
    }

    @Test
    void shouldGetCurrencyRateListFromDb() throws Exception {
        //Given
        List<CurrencyDto> dtos = getCurrencyDtoList();
        when(mapper.mapToCurrencyDtoList(dbService.getCurrencyRatesList())).thenReturn(dtos);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/nbp/database/tableC")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].tableNo", Matchers.is("TestTable")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].currencyIsoCode", Matchers.is("USD")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].buyRate", Matchers.is(4.5555)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].tableNo", Matchers.is("TestTable")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].currencyIsoCode", Matchers.is("CHF")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].buyRate", Matchers.is(5.5555)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].tableNo", Matchers.is("TestTable")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].currencyIsoCode", Matchers.is("EUR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].sellRate", Matchers.is(5.2000)));
    }

    private List<CurrencyDto> getCurrencyDtoList() {
        return List.of(
                new CurrencyDto(1L, "C", "TestTable", "2022-07-10",
                "USD", new BigDecimal("4.5555"), new BigDecimal("4.6666")),
                new CurrencyDto(2L, "C", "TestTable", "2022-07-10",
                        "CHF", new BigDecimal("5.5555"), new BigDecimal("5.6666")),
                new CurrencyDto(3L, "C", "TestTable", "2022-07-10",
                        "EUR", new BigDecimal("5.0000"), new BigDecimal("5.2000"))
        );
    }
}