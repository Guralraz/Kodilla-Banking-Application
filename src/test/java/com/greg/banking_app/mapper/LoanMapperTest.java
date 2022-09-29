package com.greg.banking_app.mapper;

import com.greg.banking_app.domain.Account;
import com.greg.banking_app.domain.Loan;
import com.greg.banking_app.dto.loan.LoanBaseDto;
import com.greg.banking_app.dto.loan.LoanCreateDto;
import com.greg.banking_app.enums.CurrencySymbol;
import com.greg.banking_app.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class LoanMapperTest {

    @Autowired
    private LoanMapper mapper;

    @MockBean
    private AccountRepository accountRepository;

    private List<Loan> prepareLoansList() {
        Account account = new Account();
        return List.of(
                new Loan(1L, new BigDecimal(1000), new BigDecimal(900), new BigDecimal(10),
                        LocalDate.of(2022, 6, 10), LocalDate.of(2023, 6, 10),
                        12, CurrencySymbol.PLN, true, account),
                new Loan(2L, new BigDecimal(2000), new BigDecimal(900), new BigDecimal(10),
                        LocalDate.of(2022, 6, 10), LocalDate.of(2023, 6, 10),
                        12, CurrencySymbol.PLN, true, account),
                new Loan(3L, new BigDecimal(1000), BigDecimal.ZERO, new BigDecimal(10),
                        LocalDate.of(2021, 6, 10), LocalDate.of(2022, 5, 10),
                        12, CurrencySymbol.PLN, false, account)
        );
    }

    @Test
    void mapToLoanTest() throws Exception {
        //Given
        Account account = new Account();
        LoanBaseDto baseDto = new LoanBaseDto.LoanBaseDtoBuilder()
                .loanId(10L)
                .startValue(new BigDecimal(2000))
                .currentValue(new BigDecimal(1800))
                .interestRate(new BigDecimal(10))
                .startDate(LocalDate.of(2022, 5, 10))
                .endDate(LocalDate.of(2024, 4, 10))
                .period(24)
                .currencySymbol(CurrencySymbol.PLN)
                .active(true)
                .accountId(1L)
                .build();
        when(accountRepository.findById(baseDto.getAccountId())).thenReturn(Optional.of(account));
        //When
        Loan loan = mapper.mapToLoan(baseDto);
        //Then
        assertEquals(10, loan.getLoanId());
        assertEquals(24, loan.getPeriod());
        assertNotEquals(CurrencySymbol.CHF, loan.getCurrencySymbol());
    }

    @Test
    void mapToLoanBaseDtoListTest() {
        //Given
        List<Loan> loans = prepareLoansList();
        //When
        List<LoanBaseDto> dtos = mapper.mapToLoanBaseDtoList(loans);
        List<LoanBaseDto> activeDtos = dtos.stream()
                .filter(dto -> dto.isActive())
                .collect(Collectors.toList());
        //Then
        assertEquals(3, dtos.size());
        assertEquals(2, activeDtos.size());
        assertNotEquals(new BigDecimal(1200), dtos.get(0).getStartValue());
        assertNotEquals(LocalDate.of(2020, 6, 10), dtos.get(1).getStartDate());
        assertFalse(dtos.get(2).isActive());
        assertTrue(activeDtos.get(0).isActive());
    }

    @Test
    void mapToLoanBaseDtoTest() {
        //Given
        Loan loan = prepareLoansList().get(0);
        //When
        LoanBaseDto dto = mapper.mapToLoanBaseDto(loan);
        //Then
        assertNotEquals(new BigDecimal(1200), dto.getStartValue());
        assertNotEquals(LocalDate.of(2020, 6, 10), dto.getStartDate());
        assertTrue(dto.isActive());
        assertEquals(LocalDate.of(2023, 6, 10), dto.getEndDate());
        assertEquals(CurrencySymbol.PLN, dto.getCurrencySymbol());
    }

    @Test
    void mapToLoanCreateTest() throws Exception {
        //Given
        LoanCreateDto createDto =
                new LoanCreateDto(new BigDecimal(2000), new BigDecimal(10), 24, CurrencySymbol.EUR, 10L);
        Account account = new Account();
        when(accountRepository.findById(createDto.getAccountId())).thenReturn(Optional.of(account));
        //When
        Loan loan = mapper.mapToLoanCreate(createDto);
        //Then
        assertEquals(LocalDate.now(), loan.getStartDate());
        assertEquals(new BigDecimal(2200), loan.getCurrentValue());
        assertEquals(LocalDate.now().plusMonths(24), loan.getEndDate());
        assertNotEquals(CurrencySymbol.PLN, loan.getCurrencySymbol());
        assertTrue(loan.isActive());
    }
}