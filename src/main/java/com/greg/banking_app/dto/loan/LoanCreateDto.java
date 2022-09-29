package com.greg.banking_app.dto.loan;

import com.greg.banking_app.enums.CurrencySymbol;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class LoanCreateDto {
    private BigDecimal startValue;
    private BigDecimal interestRate;
    private int period;
    private CurrencySymbol currencySymbol;
    private Long accountId;
}
