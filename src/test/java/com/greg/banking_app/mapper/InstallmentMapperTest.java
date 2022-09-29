package com.greg.banking_app.mapper;

import com.greg.banking_app.domain.Account;
import com.greg.banking_app.domain.Installment;
import com.greg.banking_app.domain.Loan;
import com.greg.banking_app.dto.installment.InstallmentDto;
import com.greg.banking_app.enums.CurrencySymbol;
import com.greg.banking_app.repository.LoanRepository;
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
class InstallmentMapperTest {

    @Autowired
    private InstallmentMapper mapper;

    @MockBean
    private LoanRepository loanRepository;

    private List<Installment> prepareInstallmentList() {
        Loan loan = new Loan();
        return List.of(
                new Installment(1L, new BigDecimal(100), new BigDecimal(0),
                        LocalDate.of(2022, 7, 10), LocalDate.of(2022, 7, 10), true, loan),
                new Installment(2L, new BigDecimal(100), new BigDecimal(100),
                LocalDate.of(2022, 8, 10), null, false, loan),
                new Installment(3L, new BigDecimal(100), new BigDecimal(100),
                LocalDate.of(2022, 9, 10), null, false, loan));
    }

    @Test
    void mapToInstallmentTest() throws Exception {
        //Given
        InstallmentDto installmentDto = new InstallmentDto(
                1L, new BigDecimal(100), new BigDecimal(100), LocalDate.of(2022, 8, 10),
                null, false, 10L);
        Loan loan = new Loan();
        when(loanRepository.findById(installmentDto.getLoanId())).thenReturn(Optional.of(loan));
        //When
        Installment installment = mapper.mapToInstallment(installmentDto);
        //Then
        assertEquals(1, installment.getInstallmentId());
        assertFalse(installment.isPaid());
        assertNull(installment.getPaymentDate());
        assertNotEquals(new BigDecimal(120), installment.getInstallmentValue());
    }

    @Test
    void mapToInstallmentDtoTest() {
        //Given
        Account account = new Account();
        Loan loan = new Loan(1L, new BigDecimal(1000), new BigDecimal(900), new BigDecimal(10),
                LocalDate.of(2022, 6, 10), LocalDate.of(2023, 6, 10),
                12, CurrencySymbol.PLN, true, account);
        Installment installment = new Installment(
                10L, new BigDecimal(100), new BigDecimal(100), LocalDate.of(2022, 8, 10),
                null, false, loan);
        //When
        InstallmentDto dto = mapper.mapToInstallmentDto(installment);
        //Then
        assertEquals(1, dto.getLoanId());
        assertEquals(LocalDate.of(2022, 8, 10), dto.getDueDate());
        assertNull(dto.getPaymentDate());
        assertNotEquals(new BigDecimal(200), dto.getInstallmentValue());
    }

    @Test
    void mapToInstallmentDtoListTest() {
        //Given
        List<Installment> installments = prepareInstallmentList();
        //When
        List<InstallmentDto> dtos = mapper.mapToInstallmentDtoList(installments);
        List<InstallmentDto> nonPaidDtos = dtos.stream()
                .filter(dto -> !dto.isPaid())
                .collect(Collectors.toList());
        //Then
        assertEquals(3, dtos.size());
        assertEquals(2, nonPaidDtos.size());
        assertNotEquals(3, dtos.get(0).getInstallmentId());
        assertNotEquals(new BigDecimal(200), dtos.get(1).getInstallmentValue());
    }
}