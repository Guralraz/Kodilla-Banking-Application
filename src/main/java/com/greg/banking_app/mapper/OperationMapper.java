package com.greg.banking_app.mapper;

import com.greg.banking_app.domain.Account;
import com.greg.banking_app.domain.Operation;
import com.greg.banking_app.dto.operation.OperationCreateDto;
import com.greg.banking_app.dto.operation.OperationDto;
import com.greg.banking_app.exception.AccountNotFoundException;
import com.greg.banking_app.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OperationMapper {

    @Autowired
    private AccountRepository accountRepository;

    public OperationDto mapToOperationDto(final Operation operation) {
        return new OperationDto(
                operation.getOperationId(),
                operation.getOperationType(),
                operation.getOperationValue(),
                operation.getCurrencySymbol(),
                operation.getOperationDate(),
                operation.getAccount().getAccountId()
        );
    }

    public List<OperationDto> mapToOperationDtoList(final List<Operation> list) {
        return list.stream()
                .map(this::mapToOperationDto)
                .collect(Collectors.toList());
    }

    public Operation mapToCreateOperation(final OperationCreateDto operationCreateDto) throws AccountNotFoundException {
        Account account = accountRepository.findById(operationCreateDto.getAccountId()).orElseThrow(AccountNotFoundException::new);
        return new Operation(
                operationCreateDto.getOperationType(),
                operationCreateDto.getOperationValue(),
                operationCreateDto.getCurrencySymbol(),
                account
        );
    }
}
