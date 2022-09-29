package com.greg.banking_app.dto.currency;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class CurrencyDto {

    private Long currencyId;
    private String table;
    private String tableNo;
    private String tableDate;
    private String currencyIsoCode;
    private BigDecimal buyRate;
    private BigDecimal sellRate;
}
