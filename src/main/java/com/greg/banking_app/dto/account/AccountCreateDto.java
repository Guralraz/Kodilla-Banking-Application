package com.greg.banking_app.dto.account;

import com.greg.banking_app.enums.CurrencySymbol;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountCreateDto {
    private CurrencySymbol currencySymbol;
    private Long userId;
}
