package com.greg.banking_app.domain;

import com.greg.banking_app.enums.CurrencySymbol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LOANS")
public class Loan {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID_LOAN", unique = true)
    private Long loanId;

    @NotNull
    @Column(name = "START_VALUE")
    private BigDecimal startValue;

    @NotNull
    @Column(name = "CURRENT_VALUE")
    private BigDecimal currentValue;

    @NotNull
    @Column(name = "INTEREST_RATE")
    private BigDecimal interestRate;

    @NotNull
    @Column(name = "START_DATE")
    private LocalDate startDate;

    @NotNull
    @Column(name = "END_DATE")
    private LocalDate endDate;

    @NotNull
    @Column(name = "PERIOD")
    private int period;

    @NotNull
    @Column(name = "CURRENCY")
    private CurrencySymbol currencySymbol;

    @NotNull
    @Column(name = "IS_ACTIVE")
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "ID_ACCOUNT")
    private Account account;

    @OneToMany(
            targetEntity = Installment.class,
            mappedBy = "loan",
            cascade = CascadeType.MERGE,
            fetch = FetchType.LAZY
    )
    private List<Installment> installments;

    public Loan(Long loanId, BigDecimal startValue, BigDecimal currentValue, BigDecimal interestRate, LocalDate startDate, LocalDate endDate, int period, CurrencySymbol currencySymbol, boolean active, Account account) {
        this.loanId = loanId;
        this.startValue = startValue;
        this.currentValue = currentValue;
        this.interestRate = interestRate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.period = period;
        this.currencySymbol = currencySymbol;
        this.active = active;
        this.account = account;
    }

    public Loan(BigDecimal startValue, BigDecimal interestRate, int period, CurrencySymbol currencySymbol, Account account) {
        this.startValue =  startValue;
        this.currentValue = startValue.add((startValue.multiply(interestRate)).divide(new BigDecimal(100)));
        this.interestRate = interestRate;
        this.startDate = LocalDate.now();
        this.endDate = LocalDate.now().plusMonths(period);
        this.period = period;
        this.currencySymbol = currencySymbol;
        this.active = true;
        this.account = account;
    }
}
