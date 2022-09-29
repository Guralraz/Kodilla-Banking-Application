package com.greg.banking_app.service;

import com.greg.banking_app.domain.CurrencyRates;
import com.greg.banking_app.exception.CurrencyNotFoundException;
import com.greg.banking_app.repository.CurrencyRatesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyDbService {

    private final CurrencyRatesRepository currencyRatesRepository;

    public void saveCurrencyRates(final List<CurrencyRates> currencies) {
        log.info("I am starting to save data from the NBP to the database...");
        for (CurrencyRates currentCurrency : currencies) {
            if (currencyRatesRepository.existsByCurrencyIsoCode(currentCurrency.getCurrencyIsoCode())) {
                log.info("The currency with the ISO code " + currentCurrency.getCurrencyIsoCode()
                        + " already exists in the database. I overwrite the data ...");
                CurrencyRates currencyRates = currencyRatesRepository.findByCurrencyIsoCode(currentCurrency.getCurrencyIsoCode());
                currencyRates.setTable(currentCurrency.getTable());
                currencyRates.setTableDate(currentCurrency.getTableDate());
                currencyRates.setTableNo(currentCurrency.getTableNo());
                currencyRates.setBuyRate(currentCurrency.getBuyRate());
                currencyRates.setSellRate(currentCurrency.getSellRate());
                currencyRatesRepository.save(currencyRates);
            } else {
                log.info("The currency with the ISO code "  + currentCurrency.getCurrencyIsoCode()
                        + " does not exist in the database. I'm saving the data ...");
                currencyRatesRepository.save(currentCurrency);
            }
        }
    }

    public CurrencyRates getCurrencyRate(String isoCode) throws CurrencyNotFoundException {
        if(currencyRatesRepository.existsByCurrencyIsoCode(isoCode)) {
            return currencyRatesRepository.findByCurrencyIsoCode(isoCode);
        } else {
            throw new CurrencyNotFoundException();
        }
    }

    public List<CurrencyRates> getCurrencyRatesList() {
        return currencyRatesRepository.findAll();
    }
}
