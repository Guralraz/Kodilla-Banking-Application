package com.greg.banking_app.dto.operation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OperationRangeDto {
    private LocalDateTime rangeStartDate;
    private LocalDateTime rangeEndDate;
    private Long accountId;
}
