package com.greg.banking_app.dto.installment;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class InstallmentDto {

    private Long installmentId;
    private BigDecimal installmentValue;
    private BigDecimal leftToPay;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private boolean paid;
    private Long loanId;
}
