package com.greg.banking_app.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CURRENCY_VALUES")
public class CurrencyRates {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID_CURRENCY", unique = true)
    private Long currencyId;

    @Column(name = "TABLE_NBP")
    private String table;

    @Column(name = "TABLE_NO")
    private String tableNo;

    @Column(name = "DATE")
    private String tableDate;

    @Column(name = "ISO_CODE", unique = true)
    private String currencyIsoCode;

    @Column(name = "BUY_RATE")
    private BigDecimal buyRate;

    @Column(name = "SELL_RATE")
    private BigDecimal sellRate;

    public CurrencyRates(String table, String tableNo, String tableDate, String currencyIsoCode, BigDecimal buyRate, BigDecimal sellRate) {
        this.table = table;
        this.tableNo = tableNo;
        this.tableDate = tableDate;
        this.currencyIsoCode = currencyIsoCode;
        this.buyRate = buyRate;
        this.sellRate = sellRate;
    }
}
