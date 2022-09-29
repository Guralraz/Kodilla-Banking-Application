package com.greg.banking_app.domain;

import com.greg.banking_app.enums.CurrencySymbol;
import com.greg.banking_app.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "OPERATIONS")
public class Operation {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID_OPERATION", unique = true)
    private Long operationId;

    @NotNull
    @Column(name = "OPERATION_TYPE")
    private OperationType operationType;

    @NotNull
    @Column(name = "OPERATION_VALUE")
    private BigDecimal operationValue;

    @NotNull
    @Column(name = "CURRENCY")
    private CurrencySymbol currencySymbol;

    @NotNull
    @Column(name = "DATE")
    private LocalDateTime operationDate;

    @ManyToOne
    @JoinColumn(name = "ID_ACCOUNT")
    private Account account;

    public Operation(OperationType operationType, BigDecimal operationValue, CurrencySymbol currencySymbol, Account account) {
        this.operationType = operationType;
        this.operationValue = operationValue;
        this.currencySymbol = currencySymbol;
        this.operationDate = LocalDateTime.now();
        this.account = account;
    }
}
