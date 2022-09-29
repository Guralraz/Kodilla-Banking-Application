package com.greg.banking_app.mapper;

import com.greg.banking_app.domain.Account;
import com.greg.banking_app.domain.User;
import com.greg.banking_app.dto.account.AccountBaseDto;
import com.greg.banking_app.dto.account.AccountCreateDto;
import com.greg.banking_app.enums.CurrencySymbol;
import com.greg.banking_app.repository.UserRepository;
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
class AccountMapperTest {

    @Autowired
    private AccountMapper accountMapper;

    @MockBean
    private UserRepository userRepository;

    private List<Account> prepareAccountsList() {
        User user = new User();
        return List.of(
                new Account(1L, "123456789", LocalDate.of(2022, 7, 1), null,
                        new BigDecimal(100), CurrencySymbol.USD, true, user),
                new Account(2L, "123456780", LocalDate.of(2022, 7, 1),
                        LocalDate.of(2022, 7, 23), BigDecimal.ZERO, CurrencySymbol.PLN, false, user),
                new Account(3L, "123456700", LocalDate.of(2022, 7, 10), null,
                        new BigDecimal(300), CurrencySymbol.USD, true, user));
    }

    @Test
    void mapToAccountBaseDtoTest() {
        //Given
        Account account = prepareAccountsList().get(0);
        //When
        AccountBaseDto baseDto = accountMapper.mapToAccountBaseDto(account);
        //Then
        assertEquals("123456789", baseDto.getAccountNumber());
        assertEquals(new BigDecimal(100), baseDto.getPresentValue());
        assertNotEquals(CurrencySymbol.PLN, baseDto.getCurrencySymbol());
        assertTrue(baseDto.isActive());
    }

    @Test
    void mapToAccountBaseDtoListTest() {
        //Given
        List<Account> accounts = prepareAccountsList();
        //When
        List<AccountBaseDto> dtos = accountMapper.mapToAccountBaseDtoList(accounts);
        List<AccountBaseDto> activeDtos = dtos.stream()
                .filter(dto -> dto.isActive())
                .collect(Collectors.toList());
        //Then
        assertEquals(3, dtos.size());
        assertEquals(2, activeDtos.size());
        assertEquals("123456789", dtos.get(0).getAccountNumber());
        assertNotEquals("123456789", dtos.get(1).getAccountNumber());
        assertNull(activeDtos.get(1).getCloseDate());
    }

    @Test
    void mapToCreateAccount() throws Exception {
        //Given
        AccountCreateDto createDto = new AccountCreateDto(CurrencySymbol.EUR, 1L);
        User user = new User(1L, "123456789", "John", "Doe", "TestEmail",
                "12345", true);
        when(userRepository.findById(createDto.getUserId())).thenReturn(Optional.of(user));
        //When
        Account account = accountMapper.mapToCreateAccount(createDto);
        //Then
        assertEquals(CurrencySymbol.EUR, account.getCurrencySymbol());
    }
}