package com.greg.banking_app.controller;

import com.google.gson.Gson;
import com.greg.banking_app.domain.Account;
import com.greg.banking_app.domain.User;
import com.greg.banking_app.dto.account.AccountBaseDto;
import com.greg.banking_app.dto.account.AccountCreateDto;
import com.greg.banking_app.enums.CurrencySymbol;
import com.greg.banking_app.mapper.AccountMapper;
import com.greg.banking_app.service.AccountDbService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountDbService accountDbService;

    @MockBean
    private AccountMapper accountMapper;

    private AccountBaseDto getAccountBaseDto() {
        return new AccountBaseDto(
                1L, "123456789", LocalDate.of(2022, 7, 11),
                null, new BigDecimal("100"), CurrencySymbol.PLN, true, 10L);
    }

    @Test
    void shouldReturnEmptyListUserAccounts() throws Exception {
        //Given
        when(accountMapper.mapToAccountBaseDtoList(accountDbService.getUserAccounts(anyLong()))).thenReturn(List.of());
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/accounts/user/{userId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldReturnListUserAccounts() throws Exception {
        //Given
        List<AccountBaseDto> dtos = List.of(
                getAccountBaseDto(),
                new AccountBaseDto(
                        2L, "123456700", LocalDate.of(2022, 3, 11),
                        LocalDate.of(2022, 5, 14), new BigDecimal("200"), CurrencySymbol.EUR, false, 10L));
        when(accountMapper.mapToAccountBaseDtoList(accountDbService.getUserAccounts(anyLong()))).thenReturn(dtos);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/accounts/user/{userId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountNumber", Matchers.is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].active", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].userId", Matchers.is(10)));
    }

    @Test
    void shouldGetUserAccount() throws Exception {
        //Given
        AccountBaseDto dto = getAccountBaseDto();
        when(accountMapper.mapToAccountBaseDto(accountDbService.getUserAccount(anyLong(), anyLong()))).thenReturn(dto);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/accounts/{userId}/{accountId}", 1, 2)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", Matchers.is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencySymbol", Matchers.is("PLN")));
    }

    @Test
    void shouldCreateAccount() throws Exception {
        //Given
        AccountCreateDto createDto = new AccountCreateDto(CurrencySymbol.EUR, 1L);
        User user = new User(1L, "123456789", "John", "Smith", "email", "11111", true);
        Account account = new Account(1L, "12345", LocalDate.of(2022,7,11), null, BigDecimal.ZERO, CurrencySymbol.EUR, true, user);
        AccountBaseDto dto = getAccountBaseDto();
        when(accountMapper.mapToCreateAccount(any(AccountCreateDto.class))).thenReturn(account);
        when(accountMapper.mapToAccountBaseDto(accountDbService.createAccount(any(Account.class)))).thenReturn(dto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(createDto);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", Matchers.is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", Matchers.is(10)));
    }

    @Test
    void shouldDeActiveAccount() throws Exception {
        //Given
        Mockito.doNothing().when(accountDbService).deActiveAccount(anyLong());
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/v1/accounts/{accountId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.is("Account is deActivated")));
    }
}