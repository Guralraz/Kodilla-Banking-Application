package com.greg.banking_app.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.greg.banking_app.domain.Loan;
import com.greg.banking_app.dto.loan.LoanBaseDto;
import com.greg.banking_app.dto.loan.LoanCreateDto;
import com.greg.banking_app.enums.CurrencySymbol;
import com.greg.banking_app.mapper.LoanMapper;
import com.greg.banking_app.service.LoanDbService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
@WebMvcTest(LoanController.class)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanDbService loanDbService;

    @MockBean
    private LoanMapper loanMapper;

    private LoanBaseDto getLoanBaseDto() {
        return new LoanBaseDto(1L, new BigDecimal(1000), new BigDecimal(900), new BigDecimal(10),
                LocalDate.of(2022, 6, 10), LocalDate.of(2023, 6, 10),
                12, CurrencySymbol.PLN, true, 10L);
    }

    @Test
    void shouldReturnEmptyLoansList() throws Exception {
        //Given
        when(loanMapper.mapToLoanBaseDtoList(loanDbService.getAccountLoans(anyLong()))).thenReturn(List.of());
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/loans/account/{accountId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldReturnLoansList() throws Exception {
        //Given
        List<LoanBaseDto> dtos = List.of(
                getLoanBaseDto(),
                new LoanBaseDto(2L, new BigDecimal(2000), new BigDecimal(1000), new BigDecimal(10),
                        LocalDate.of(2021, 6, 10), LocalDate.of(2023, 6, 10),
                        24, CurrencySymbol.PLN, true, 10L));
        when(loanMapper.mapToLoanBaseDtoList(loanDbService.getAccountLoans(anyLong()))).thenReturn(dtos);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/loans/account/{accountId}", 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].startValue", Matchers.is(1000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].startDate", Matchers.is("2022-06-10")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].currencySymbol", Matchers.is("PLN")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].accountId", Matchers.is(10)));
    }

    @Test
    void shouldReturnLoan() throws Exception {
        //Given
        LoanBaseDto dto = getLoanBaseDto();
        when(loanMapper.mapToLoanBaseDto(loanDbService.getLoan(anyLong()))).thenReturn(dto);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/loans/loan/{loanId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startValue", Matchers.is(1000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", Matchers.is("2022-06-10")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencySymbol", Matchers.is("PLN")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId", Matchers.is(10)));
    }

    @Test
    void shouldCreateLoan() throws Exception {
        //Given
        LoanBaseDto dto = getLoanBaseDto();
        LoanCreateDto createDto = new LoanCreateDto(new BigDecimal(1000), new BigDecimal(10),
                12, CurrencySymbol.PLN, 10L);
        Loan loan = new Loan();
        when(loanMapper.mapToLoanCreate(any(LoanCreateDto.class))).thenReturn(loan);
        when(loanMapper.mapToLoanBaseDto(loanDbService.createLoan(any(Loan.class)))).thenReturn(dto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(createDto);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startValue", Matchers.is(1000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", Matchers.is("2022-06-10")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencySymbol", Matchers.is("PLN")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId", Matchers.is(10)));
    }

    @Test
    void shouldDeActiveLoan() throws Exception {
        //Given
        Mockito.doNothing().when(loanDbService).deActiveLoan(anyLong());
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/v1/loans/{loanId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.is("Loan is deActivated")));
    }

    @Test
    void shouldUpdateLoan() throws Exception {
        //Given
        LoanBaseDto dto = getLoanBaseDto();
        Loan loan = new Loan();
        when(loanMapper.mapToLoan(any(LoanBaseDto.class))).thenReturn(loan);
        when(loanMapper.mapToLoanBaseDto(loanDbService.updateLoan(any(Loan.class)))).thenReturn(dto);

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateSerializer()).setPrettyPrinting().create();
        String jsonContent = gson.toJson(dto);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startValue", Matchers.is(1000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", Matchers.is("2022-06-10")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencySymbol", Matchers.is("PLN")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId", Matchers.is(10)));
    }
}