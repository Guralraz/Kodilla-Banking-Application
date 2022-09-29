package com.greg.banking_app.service;

import com.greg.banking_app.domain.Operation;
import com.greg.banking_app.enums.OperationType;
import com.greg.banking_app.exception.AccountNotFoundException;
import com.greg.banking_app.exception.OperationNotFoundException;
import com.greg.banking_app.repository.AccountRepository;
import com.greg.banking_app.repository.OperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationDbService {

    private final OperationRepository operationRepository;
    private final AccountRepository accountRepository;

    public List<Operation> getAccountOperations(final Long accountId) throws AccountNotFoundException {
        if(accountRepository.existsById(accountId)) {
            return operationRepository.findByAccount_AccountId(accountId);
        } else {
            throw new AccountNotFoundException();
        }
    }

    public Operation getOperation(final Long operationId) throws OperationNotFoundException {
        return operationRepository.findById(operationId).orElseThrow(OperationNotFoundException::new);
    }

    public List<Operation> getAccountDateRangeOperations(
            final Long accountId, final LocalDateTime rangeStart, final LocalDateTime rangeEnd) throws AccountNotFoundException {
        if(accountRepository.existsById(accountId)) {
            return operationRepository.findByAccount_AccountIdAndOperationDateIsBetweenOrderByOperationDate(
                    accountId, rangeStart, rangeEnd
            );
        } else {
            throw new AccountNotFoundException();
        }
    }

    public List<Operation> getAccountFromDateAndTypeOperations(
            final Long accountId, final LocalDateTime fromDate, final OperationType operationType) throws AccountNotFoundException {
        if(accountRepository.existsById(accountId)) {
            return operationRepository.findByAccount_AccountIdAndOperationDateAfterAndOperationType(
                    accountId, fromDate, operationType
            );
        } else {
            throw new AccountNotFoundException();
        }
    }

    public Operation createOperation(final Operation operation) {
            return operationRepository.save(operation);
    }
}
