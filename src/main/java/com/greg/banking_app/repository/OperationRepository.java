package com.greg.banking_app.repository;

import com.greg.banking_app.domain.Operation;
import com.greg.banking_app.enums.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public interface OperationRepository extends JpaRepository<Operation, Long> {

    List<Operation> findByAccount_AccountIdAndOperationDateAfterAndOperationType(Long accountId, LocalDateTime startDate, OperationType operationType);
    List<Operation> findByAccount_AccountId(Long accountId);
    List<Operation> findByAccount_AccountIdAndOperationDateIsBetweenOrderByOperationDate(Long accountId, LocalDateTime rangeStart, LocalDateTime rangeEnd);
}
