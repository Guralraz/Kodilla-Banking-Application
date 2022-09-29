package com.greg.banking_app.dto.loan;

import com.greg.banking_app.enums.CurrencySymbol;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class LoanBaseDto {

    private Long loanId;
    private BigDecimal startValue;
    private BigDecimal currentValue;
    private BigDecimal interestRate;
    private LocalDate startDate;
    private LocalDate endDate;
    private int period;
    private CurrencySymbol currencySymbol;
    private boolean active;
    private Long accountId;

    public static class LoanBaseDtoBuilder {
        private Long loanId;
        private BigDecimal startValue;
        private BigDecimal currentValue;
        private BigDecimal interestRate;
        private LocalDate startDate;
        private LocalDate endDate;
        private int period;
        private CurrencySymbol currencySymbol;
        private boolean active;
        private Long accountId;

        public LoanBaseDto.LoanBaseDtoBuilder loanId(Long loanId) {
            this.loanId = loanId;
            return this;
        }

        public LoanBaseDto.LoanBaseDtoBuilder startValue(BigDecimal startValue) {
            this.startValue = startValue;
            return this;
        }

        public LoanBaseDto.LoanBaseDtoBuilder currentValue(BigDecimal currentValue) {
            this.currentValue = currentValue;
            return this;
        }

        public LoanBaseDto.LoanBaseDtoBuilder interestRate(BigDecimal interestRate) {
            this.interestRate = interestRate;
            return this;
        }

        public LoanBaseDto.LoanBaseDtoBuilder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public LoanBaseDto.LoanBaseDtoBuilder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public LoanBaseDto.LoanBaseDtoBuilder period(int period) {
            this.period = period;
            return this;
        }

        public LoanBaseDto.LoanBaseDtoBuilder currencySymbol(CurrencySymbol currencySymbol) {
            this.currencySymbol = currencySymbol;
            return this;
        }

        public LoanBaseDto.LoanBaseDtoBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public LoanBaseDto.LoanBaseDtoBuilder accountId(Long accountId) {
            this.accountId = accountId;
            return this;
        }

        public LoanBaseDto build() {
            return new LoanBaseDto(
                    loanId, startValue, currentValue, interestRate, startDate, endDate, period, currencySymbol, active, accountId
            );
        }
    }
}

