package com.greg.banking_app.utils;

import com.greg.banking_app.domain.Installment;
import com.greg.banking_app.domain.Loan;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InstallmentRepaymentScheduleCreator {

    public List<Installment> createRepaymentSchedule(final Loan loan) {
        List<Installment> list = new ArrayList<>();
        BigDecimal loanValue = loan.getCurrentValue();
        int n = loan.getPeriod();
        BigDecimal period = new BigDecimal(loan.getPeriod());
        BigDecimal installmentValue = loanValue.divide(period, 2, RoundingMode.HALF_UP);
        for(int i = 1; i <= n; i++) {
            LocalDate date = LocalDate.now().plusMonths(i);
            Installment currentInstallment = new Installment(installmentValue, date, loan);
            list.add(currentInstallment);
        }
        return list;
    }
}
