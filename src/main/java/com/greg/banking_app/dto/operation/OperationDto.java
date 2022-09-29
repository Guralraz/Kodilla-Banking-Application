package com.greg.banking_app.dto.operation;

import com.greg.banking_app.enums.CurrencySymbol;
import com.greg.banking_app.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OperationDto {

    private Long operationId;
    private OperationType operationType;
    private BigDecimal operationValue;
    private CurrencySymbol currencySymbol;
    private LocalDateTime operationDate;
    private Long accountId;
}
