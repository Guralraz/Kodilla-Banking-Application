package com.greg.banking_app.controller;

import com.greg.banking_app.domain.Operation;
import com.greg.banking_app.dto.operation.OperationCreateDto;
import com.greg.banking_app.dto.operation.OperationDateTypeDto;
import com.greg.banking_app.dto.operation.OperationDto;
import com.greg.banking_app.dto.operation.OperationRangeDto;
import com.greg.banking_app.exception.AccountNotFoundException;
import com.greg.banking_app.exception.OperationNotFoundException;
import com.greg.banking_app.mapper.OperationMapper;
import com.greg.banking_app.service.OperationDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "v1/operations")
@RequiredArgsConstructor
public class OperationController {

    private final OperationDbService operationDbService;
    private final OperationMapper operationMapper;

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<OperationDto>> getOperations(@PathVariable Long accountId) throws AccountNotFoundException {
        return ResponseEntity.ok(operationMapper.mapToOperationDtoList(operationDbService.getAccountOperations(accountId)));
    }

    @GetMapping(value = "dateRange", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OperationDto>> getDateRangeOperations(@RequestBody OperationRangeDto operationRangeDto) throws AccountNotFoundException {
       return ResponseEntity.ok(operationMapper.mapToOperationDtoList(
               operationDbService.getAccountDateRangeOperations(
                       operationRangeDto.getAccountId(), operationRangeDto.getRangeStartDate(), operationRangeDto.getRangeEndDate())));
    }

    @GetMapping(value = "date/type", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OperationDto>> getDateAndTypeOperations(@RequestBody OperationDateTypeDto operationDateTypeDto) throws AccountNotFoundException {
        return ResponseEntity.ok(operationMapper.mapToOperationDtoList(
                operationDbService.getAccountFromDateAndTypeOperations(
                        operationDateTypeDto.getAccountId(), operationDateTypeDto.getDate(), operationDateTypeDto.getOperationType())));
    }

    @GetMapping("/operation/{operationId}")
    public ResponseEntity<OperationDto> getOperation(@PathVariable Long operationId) throws OperationNotFoundException {
        return ResponseEntity.ok(operationMapper.mapToOperationDto(operationDbService.getOperation(operationId)));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OperationDto> addOperation(@RequestBody OperationCreateDto operationDto) throws AccountNotFoundException {
        Operation operation = operationMapper.mapToCreateOperation(operationDto);
        return ResponseEntity.ok(operationMapper.mapToOperationDto(operationDbService.createOperation(operation)));
    }
}
