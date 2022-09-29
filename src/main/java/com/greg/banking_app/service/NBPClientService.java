package com.greg.banking_app.service;

import com.greg.banking_app.client.NBPClient;
import com.greg.banking_app.domain.CurrencyRates;
import com.greg.banking_app.dto.NBPClient.NBPTableCDto;
import com.greg.banking_app.mapper.CurrencyRatesMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NBPClientService {

    private final NBPClient client;
    private final CurrencyRatesMapper mapper;
    private final CurrencyDbService currencyDbService;

    public void fetchTableCFromNbp() {
        log.info("Table C data download service from NBP");
        List<NBPTableCDto> list = client.getNBPTableC();
        List<CurrencyRates> currencies = mapper.mapToCurrencyValue(list.get(0));
        currencyDbService.saveCurrencyRates(currencies);
    }
}
