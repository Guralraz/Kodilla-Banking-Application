package com.greg.banking_app.dto.operation;

import com.greg.banking_app.enums.CurrencySymbol;
import com.greg.banking_app.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class OperationCreateDto {
    private OperationType operationType;
    private BigDecimal operationValue;
    private CurrencySymbol currencySymbol;
    private Long accountId;
}
