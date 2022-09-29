package com.greg.banking_app.service;

import com.greg.banking_app.domain.Loan;
import com.greg.banking_app.exception.AccountNotFoundException;
import com.greg.banking_app.exception.LoanNotCreatedException;
import com.greg.banking_app.exception.LoanNotFoundException;
import com.greg.banking_app.repository.AccountRepository;
import com.greg.banking_app.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanDbService {

    private final LoanRepository loanRepository;
    private final AccountRepository accountRepository;
    private final InstallmentDbService installmentDbService;

    public List<Loan> getAccountLoans(final Long accountId) throws AccountNotFoundException {
        if(accountRepository.existsById(accountId)) {
            return loanRepository.findByAccount_AccountId(accountId);
        } else {
            throw new AccountNotFoundException();
        }
    }

    public Loan getLoan(final Long loanId) throws LoanNotFoundException {
        return loanRepository.findById(loanId).orElseThrow(LoanNotFoundException::new);
    }

    public Loan createLoan(final Loan loan) throws LoanNotCreatedException {
        Loan savedLoan = loanRepository.save(loan);
        if(savedLoan.isActive()) {
            installmentDbService.createInstallments(savedLoan);
            return savedLoan;
        } else {
            throw new LoanNotCreatedException();
        }
    }

    public void deActiveLoan(final Long loanId) throws LoanNotFoundException {
        if(loanRepository.existsById(loanId)) {
            Loan currentLoan = loanRepository.findById(loanId).get();
            currentLoan.setActive(false);
            loanRepository.save(currentLoan);
        } else {
            throw new LoanNotFoundException();
        }
    }

    public Loan updateLoan(final Loan loan) throws LoanNotFoundException {
        if(loanRepository.existsById(loan.getLoanId())) {
            return loanRepository.save(loan);
        } else {
            throw new LoanNotFoundException();
        }
    }
}
