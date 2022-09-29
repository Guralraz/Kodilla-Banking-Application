package com.greg.banking_app.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.greg.banking_app.domain.Installment;
import com.greg.banking_app.domain.Loan;
import com.greg.banking_app.dto.installment.InstallmentDto;
import com.greg.banking_app.mapper.InstallmentMapper;
import com.greg.banking_app.service.InstallmentDbService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
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
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringJUnitConfig
@WebMvcTest(InstallmentController.class)
class InstallmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InstallmentDbService installmentDbService;

    @MockBean
    private InstallmentMapper installmentMapper;

    @MockBean
    private Loan loan;


    private InstallmentDto getUnpaidInstallmentDto() {
        return new InstallmentDto(1L, new BigDecimal(100), new BigDecimal(100),
                LocalDate.of(2022, 8, 10), null, false, 10L);
    }

    private InstallmentDto getPaidInstallmentDto() {
        return new InstallmentDto(1L, new BigDecimal(100), new BigDecimal(0),
                LocalDate.of(2022, 7, 10), LocalDate.of(2022, 7, 9), true, 10L);
    }

    @Test
    void shouldReturnEmptyInstallmentsList() throws Exception {
        //Given
        when(installmentMapper.mapToInstallmentDtoList(installmentDbService.getLoanInstallments(anyLong()))).thenReturn(List.of());
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/installments/loan/{loanId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldReturnInstallmentsList() throws Exception {
        //Given
        List<InstallmentDto> dtos = List.of(
                getUnpaidInstallmentDto(), getPaidInstallmentDto());
        when(installmentMapper.mapToInstallmentDtoList(installmentDbService.getLoanInstallments(anyLong()))).thenReturn(dtos);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/installments/loan/{loanId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].installmentValue", Matchers.is(100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].paymentDate", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].paid", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].paymentDate", Matchers.is("2022-07-09")));
    }

    @Test
    void shouldReturnInstallment() throws Exception {
        //Given
        InstallmentDto dto = getUnpaidInstallmentDto();
        when(installmentMapper.mapToInstallmentDto(installmentDbService.getInstallment(anyLong()))).thenReturn(dto);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/installments/{loanId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.loanId", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.installmentValue", Matchers.is(100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.paymentDate", Matchers.nullValue()));
    }


    @Test
    void shouldUpdateInstallment() throws Exception {
        //Given
        InstallmentDto dto = getPaidInstallmentDto();
        Installment installment = new Installment(new BigDecimal(100), LocalDate.of(2022, 7, 20), loan);
        when(installmentMapper.mapToInstallment(any(InstallmentDto.class))).thenReturn(installment);
        when(installmentMapper.mapToInstallmentDto(installmentDbService.updateInstallment(installment))).thenReturn(dto);

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateSerializer()).setPrettyPrinting().create();
        String jsonContent = gson.toJson(dto);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/installments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.loanId", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.installmentValue", Matchers.is(100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dueDate", Matchers.is("2022-07-10")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.paymentDate", Matchers.notNullValue()));
    }
}