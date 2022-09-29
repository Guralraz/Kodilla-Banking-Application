package com.greg.banking_app.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.greg.banking_app.domain.Operation;
import com.greg.banking_app.dto.operation.OperationCreateDto;
import com.greg.banking_app.dto.operation.OperationDateTypeDto;
import com.greg.banking_app.dto.operation.OperationDto;
import com.greg.banking_app.dto.operation.OperationRangeDto;
import com.greg.banking_app.enums.CurrencySymbol;
import com.greg.banking_app.enums.OperationType;
import com.greg.banking_app.mapper.OperationMapper;
import com.greg.banking_app.service.OperationDbService;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
@WebMvcTest(OperationController.class)
class OperationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OperationDbService operationDbService;

    @MockBean
    private OperationMapper operationMapper;

    private OperationDto getOperationDto() {
        return new OperationDto(1L, OperationType.DEPOSIT, new BigDecimal(100), CurrencySymbol.PLN,
                LocalDateTime.of(2022, 7, 11, 11, 30), 10L);
    }

    private OperationDto getOperationDtoTransfOut() {
        return new OperationDto(2L, OperationType.TRANSFER_OUT, new BigDecimal(100), CurrencySymbol.PLN,
                LocalDateTime.of(2022, 7, 12, 12, 30), 10L);
    }

    @Test
    void shouldReturnEmptyOperationsList() throws Exception {
        //Given
        when(operationMapper.mapToOperationDtoList(operationDbService.getAccountOperations(anyLong()))).thenReturn(List.of());
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/operations/account/{accountId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldReturnOperationsList() throws Exception {
        //Given
        List<OperationDto> dtos = List.of(getOperationDto(), getOperationDtoTransfOut());
        when(operationMapper.mapToOperationDtoList(operationDbService.getAccountOperations(anyLong()))).thenReturn(dtos);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/operations/account/{accountId}", 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].operationValue", Matchers.is(100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].operationDate", Matchers.is("2022-07-11T11:30:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].currencySymbol", Matchers.is("PLN")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].accountId", Matchers.is(10)));
    }


    @Test
    void shouldReturnOperation() throws Exception {
        //Given
        OperationDto dto = getOperationDto();
        when(operationMapper.mapToOperationDto(operationDbService.getOperation(anyLong()))).thenReturn(dto);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/operations/operation/{operationId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.operationValue", Matchers.is(100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.operationDate", Matchers.is("2022-07-11T11:30:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencySymbol", Matchers.is("PLN")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId", Matchers.is(10)));
    }

    @Test
    void shouldReturnDateRangeOperationsList() throws Exception {
        //Given
        List<OperationDto> dtos = List.of(getOperationDto(), getOperationDtoTransfOut());
        OperationRangeDto rangeDto =
                new OperationRangeDto(LocalDateTime.of(LocalDate.of(2022, 7, 11), LocalTime.MIDNIGHT),
                        LocalDateTime.of(LocalDate.of(2022, 7, 12), LocalTime.MAX), 10L);

        when(operationMapper.mapToOperationDtoList(
                operationDbService.getAccountDateRangeOperations(
                        anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))).thenReturn(dtos);

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer()).setPrettyPrinting().create();
        String jsonContent = gson.toJson(rangeDto);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/operations/dateRange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].operationValue", Matchers.is(100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].operationDate", Matchers.is("2022-07-11T11:30:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].currencySymbol", Matchers.is("PLN")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].accountId", Matchers.is(10)));
    }

    @Test
    void shouldReturnDateTypeOperationsList() throws Exception {
        //Given
        List<OperationDto> dtos = List.of(getOperationDto(), getOperationDtoTransfOut());
        OperationDateTypeDto dateTypeDtoDto =
                new OperationDateTypeDto(LocalDateTime.of(2022, 7, 11, 0, 0),
                        OperationType.DEPOSIT, 10L);

        when(operationMapper.mapToOperationDtoList(
                operationDbService.getAccountFromDateAndTypeOperations(
                        anyLong(), any(LocalDateTime.class), any(OperationType.class)))).thenReturn(dtos);

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer()).setPrettyPrinting().create();
        String jsonContent = gson.toJson(dateTypeDtoDto);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/operations/date/type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].operationValue", Matchers.is(100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].operationDate", Matchers.is("2022-07-11T11:30:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].currencySymbol", Matchers.is("PLN")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].accountId", Matchers.is(10)));
    }

    @Test
    void shouldAddOperation() throws Exception {
        //Given
        OperationDto dto = getOperationDto();
        OperationCreateDto createDto =
                new OperationCreateDto(OperationType.DEPOSIT, new BigDecimal(100), CurrencySymbol.PLN, 10L);
        Operation operation = new Operation();

        when(operationMapper.mapToCreateOperation(any(OperationCreateDto.class))).thenReturn(operation);
        when(operationMapper.mapToOperationDto(operationDbService.createOperation(any(Operation.class)))).thenReturn(dto);

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer()).setPrettyPrinting().create();
        String jsonContent = gson.toJson(createDto);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/operations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.operationValue", Matchers.is(100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.operationDate", Matchers.is("2022-07-11T11:30:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencySymbol", Matchers.is("PLN")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId", Matchers.is(10)));
    }
    //Given
    //When & Then
}