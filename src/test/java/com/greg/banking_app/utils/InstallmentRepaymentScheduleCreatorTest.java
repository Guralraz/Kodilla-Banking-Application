package com.greg.banking_app.utils;

import com.greg.banking_app.domain.Account;
import com.greg.banking_app.domain.Installment;
import com.greg.banking_app.domain.Loan;
import com.greg.banking_app.enums.CurrencySymbol;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
class InstallmentRepaymentScheduleCreatorTest {

    @Test
    void createRepaymentScheduleTest() {
        //Given
        Account account = mock(Account.class);
        Loan loan = new Loan(new BigDecimal(1000), new BigDecimal(10), 12, CurrencySymbol.PLN, account);
        InstallmentRepaymentScheduleCreator creator = new InstallmentRepaymentScheduleCreator();
        //When
        List<Installment> list = creator.createRepaymentSchedule(loan);
        //Then
        assertEquals(12, list.size());
        assertEquals(new BigDecimal("91.67"), list.get(0).getInstallmentValue());
        assertEquals(new BigDecimal("91.67"), list.get(11).getInstallmentValue());
        assertNotEquals(LocalDate.now(), list.get(0).getDueDate());
        assertEquals(LocalDate.now().plusMonths(1), list.get(0).getDueDate());
        assertEquals(LocalDate.now().plusMonths(10), list.get(9).getDueDate());
    }

}