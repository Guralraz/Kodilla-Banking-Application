package com.greg.banking_app.dto.operation;

import com.greg.banking_app.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OperationDateTypeDto {
    private LocalDateTime date;
    private OperationType operationType;
    private Long accountId;
}
