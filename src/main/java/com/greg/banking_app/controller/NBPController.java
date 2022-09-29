package com.greg.banking_app.controller;

import com.greg.banking_app.client.NBPClient;
import com.greg.banking_app.dto.NBPClient.NBPTableCDto;
import com.greg.banking_app.dto.currency.CurrencyDto;
import com.greg.banking_app.exception.CurrencyNotFoundException;
import com.greg.banking_app.mapper.CurrencyRatesMapper;
import com.greg.banking_app.service.CurrencyDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/nbp")
@RequiredArgsConstructor
public class NBPController {

    private final NBPClient client;
    private final CurrencyRatesMapper mapper;
    private final CurrencyDbService dbService;

    @GetMapping("tableC")
    public ResponseEntity<List<NBPTableCDto>> getNBPTable() {
        return ResponseEntity.ok(client.getNBPTableC());
    }

    @GetMapping("database/currency/{isoCode}")
    public ResponseEntity<CurrencyDto> getCurrencyRateFromDb(@PathVariable String isoCode) throws CurrencyNotFoundException {
        return ResponseEntity.ok(mapper.mapToCurrencyDto(dbService.getCurrencyRate(isoCode)));
    }

    @GetMapping("database/tableC")
    public ResponseEntity<List<CurrencyDto>> getCurrencyRatesFromDb() {
        return ResponseEntity.ok(mapper.mapToCurrencyDtoList(dbService.getCurrencyRatesList()));
    }
}
