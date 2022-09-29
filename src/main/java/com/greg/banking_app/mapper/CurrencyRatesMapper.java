package com.greg.banking_app.mapper;

import com.greg.banking_app.domain.CurrencyRates;
import com.greg.banking_app.dto.NBPClient.NBPCurrencyDto;
import com.greg.banking_app.dto.NBPClient.NBPTableCDto;
import com.greg.banking_app.dto.currency.CurrencyDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CurrencyRatesMapper {

    public List<CurrencyRates> mapToCurrencyValue(final NBPTableCDto nbpTableCDto) {
        log.info("I'm mapping data from the NBP...");
        List<CurrencyRates> list = new ArrayList<>();
        List<NBPCurrencyDto> currencies = nbpTableCDto.getRates();
        String table = nbpTableCDto.getTable();
        String tableNo = nbpTableCDto.getTableNo();
        String date = nbpTableCDto.getEffectiveDate();
        for (NBPCurrencyDto currency : currencies) {
            CurrencyRates currentCurrency = new CurrencyRates(
                    table,
                    tableNo,
                    date,
                    currency.getCurrencyCode(),
                    currency.getBuyRate(),
                    currency.getSellRate()
            );
            list.add(currentCurrency);
        }
        return list;
    }

    public CurrencyDto mapToCurrencyDto(CurrencyRates currencyRates) {
        return new CurrencyDto(
                currencyRates.getCurrencyId(),
                currencyRates.getTable(),
                currencyRates.getTableNo(),
                currencyRates.getTableDate(),
                currencyRates.getCurrencyIsoCode(),
                currencyRates.getBuyRate(),
                currencyRates.getSellRate()
        );
    }

    public List<CurrencyDto> mapToCurrencyDtoList(List<CurrencyRates> currencyRates) {
        return currencyRates.stream()
                .map(this::mapToCurrencyDto)
                .collect(Collectors.toList());
    }
}
