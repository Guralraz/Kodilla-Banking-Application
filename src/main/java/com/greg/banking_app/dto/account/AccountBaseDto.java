package com.greg.banking_app.dto.account;

import com.greg.banking_app.enums.CurrencySymbol;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class AccountBaseDto {

    private Long accountId;
    private String accountNumber;
    private LocalDate openDate;
    private LocalDate closeDate;
    private BigDecimal presentValue;
    private CurrencySymbol currencySymbol;
    private boolean active;
    private Long userId;
}
