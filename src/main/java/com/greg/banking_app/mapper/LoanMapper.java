package com.greg.banking_app.mapper;

import com.greg.banking_app.domain.Account;
import com.greg.banking_app.domain.Loan;
import com.greg.banking_app.dto.loan.LoanBaseDto;
import com.greg.banking_app.dto.loan.LoanCreateDto;
import com.greg.banking_app.exception.AccountNotFoundException;
import com.greg.banking_app.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanMapper {

    @Autowired
    private AccountRepository accountRepository;

    public Loan mapToLoan(final LoanBaseDto loanBaseDto) throws AccountNotFoundException {
        Account currentAccount = accountRepository.findById(loanBaseDto.getAccountId()).orElseThrow(AccountNotFoundException::new);
        return new Loan(
                loanBaseDto.getLoanId(),
                loanBaseDto.getStartValue(),
                loanBaseDto.getCurrentValue(),
                loanBaseDto.getInterestRate(),
                loanBaseDto.getStartDate(),
                loanBaseDto.getEndDate(),
                loanBaseDto.getPeriod(),
                loanBaseDto.getCurrencySymbol(),
                loanBaseDto.isActive(),
                currentAccount
        );
    }

    public List<LoanBaseDto> mapToLoanBaseDtoList(final List<Loan> list) {
        return list.stream()
                .map(this::mapToLoanBaseDto)
                .collect(Collectors.toList());
    }

    public LoanBaseDto mapToLoanBaseDto(final Loan loan) {
        return new LoanBaseDto(
                loan.getLoanId(),
                loan.getStartValue(),
                loan.getCurrentValue(),
                loan.getInterestRate(),
                loan.getStartDate(),
                loan.getEndDate(),
                loan.getPeriod(),
                loan.getCurrencySymbol(),
                loan.isActive(),
                loan.getAccount().getAccountId()
        );
    }

    public Loan mapToLoanCreate(final LoanCreateDto loanCreateDto) throws AccountNotFoundException {
        Account account = accountRepository.findById(loanCreateDto.getAccountId()).orElseThrow(AccountNotFoundException::new);
        return new Loan(
                loanCreateDto.getStartValue(),
                loanCreateDto.getInterestRate(),
                loanCreateDto.getPeriod(),
                loanCreateDto.getCurrencySymbol(),
                account
        );
    }
}
