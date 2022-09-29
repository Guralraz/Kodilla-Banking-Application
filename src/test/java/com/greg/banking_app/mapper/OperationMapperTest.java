package com.greg.banking_app.mapper;

import com.greg.banking_app.domain.Account;
import com.greg.banking_app.domain.Operation;
import com.greg.banking_app.dto.operation.OperationCreateDto;
import com.greg.banking_app.dto.operation.OperationDto;
import com.greg.banking_app.enums.CurrencySymbol;
import com.greg.banking_app.enums.OperationType;
import com.greg.banking_app.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class OperationMapperTest {

    @Autowired
    private OperationMapper operationMapper;

    @MockBean
    private AccountRepository accountRepository;

    private List<Operation> prepareOperationList() {
        Account account = new Account();
        return List.of(
                new Operation(1L, OperationType.DEPOSIT, new BigDecimal(100), CurrencySymbol.PLN,
                        LocalDateTime.of(2022, 7, 5, 11, 30), account),
                new Operation(2L, OperationType.WITHDRAWAL, new BigDecimal(100), CurrencySymbol.PLN,
                        LocalDateTime.of(2022, 7, 6, 11, 30), account),
                new Operation(3L, OperationType.DEPOSIT, new BigDecimal(100), CurrencySymbol.PLN,
                        LocalDateTime.of(2022, 7, 7, 11, 30), account)
        );
    }

    @Test
    void mapToOperationDtoTest() {
        //Given
        Operation operation = prepareOperationList().get(0);
        //When
        OperationDto dto = operationMapper.mapToOperationDto(operation);
        //Then
        assertEquals(1, dto.getOperationId());
        assertEquals(OperationType.DEPOSIT, dto.getOperationType());
        assertNotEquals(CurrencySymbol.EUR, dto.getCurrencySymbol());
        assertEquals(LocalDateTime.of(2022, 7, 5, 11, 30), dto.getOperationDate());
        assertNotEquals(new BigDecimal(200), dto.getOperationValue());
    }

    @Test
    void mapToOperationDtoListTest() {
        //Given
        List<Operation> operations = prepareOperationList();
        //When
        List<OperationDto> dtos = operationMapper.mapToOperationDtoList(operations);
        List<OperationDto> deposits = dtos.stream()
                .filter(dto -> dto.getOperationType().equals(OperationType.DEPOSIT))
                .collect(Collectors.toList());
        //Then
        assertEquals(3, dtos.size());
        assertEquals(2, deposits.size());
        assertEquals(1, dtos.get(0).getOperationId());
        assertEquals(OperationType.DEPOSIT, deposits.get(1).getOperationType());
        assertNotEquals(CurrencySymbol.EUR, dtos.get(2).getCurrencySymbol());
        assertEquals(LocalDateTime.of(2022, 7, 5, 11, 30), dtos.get(0).getOperationDate());
        assertNotEquals(new BigDecimal(200), deposits.get(1).getOperationValue());
    }

    @Test
    void mapToCreateOperationTest() throws Exception {
        //Given
        OperationCreateDto operationCreateDto = new OperationCreateDto(OperationType.DEPOSIT, new BigDecimal(100),
                CurrencySymbol.PLN, 10L);
        Account account = new Account();
        when(accountRepository.findById(operationCreateDto.getAccountId())).thenReturn(Optional.of(account));
        //When
        Operation operation = operationMapper.mapToCreateOperation(operationCreateDto);
        //Then
        assertEquals(OperationType.DEPOSIT, operation.getOperationType());
        assertNotEquals(CurrencySymbol.EUR, operation.getCurrencySymbol());
        assertNotEquals(new BigDecimal(200), operation.getOperationValue());
        assertEquals(new BigDecimal(100), operation.getOperationValue());
    }
}