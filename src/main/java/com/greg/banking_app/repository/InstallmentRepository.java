package com.greg.banking_app.repository;

import com.greg.banking_app.domain.Installment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface InstallmentRepository extends JpaRepository<Installment, Long> {

    List<Installment> findByLoan_LoanId(Long loanId);
}
