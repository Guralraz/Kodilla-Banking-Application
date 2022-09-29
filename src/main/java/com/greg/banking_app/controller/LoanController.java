package com.greg.banking_app.controller;

import com.greg.banking_app.domain.Loan;
import com.greg.banking_app.dto.loan.LoanBaseDto;
import com.greg.banking_app.dto.loan.LoanCreateDto;
import com.greg.banking_app.exception.AccountNotFoundException;
import com.greg.banking_app.exception.LoanNotCreatedException;
import com.greg.banking_app.exception.LoanNotFoundException;
import com.greg.banking_app.mapper.LoanMapper;
import com.greg.banking_app.service.LoanDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanDbService loanDbService;
    private final LoanMapper loanMapper;

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<LoanBaseDto>> getLoans(@PathVariable Long accountId) throws AccountNotFoundException {
        return ResponseEntity.ok(loanMapper.mapToLoanBaseDtoList(loanDbService.getAccountLoans(accountId)));
    }

    @GetMapping("loan/{loanId}")
    public ResponseEntity<LoanBaseDto> getLoan(@PathVariable Long loanId) throws LoanNotFoundException {
        return ResponseEntity.ok(loanMapper.mapToLoanBaseDto(loanDbService.getLoan(loanId)));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoanBaseDto> createLoan(@RequestBody LoanCreateDto loanCreateDto)
            throws AccountNotFoundException, LoanNotCreatedException {
        Loan loan = loanMapper.mapToLoanCreate(loanCreateDto);
        return ResponseEntity.ok(loanMapper.mapToLoanBaseDto(loanDbService.createLoan(loan)));
    }

    @DeleteMapping("{loanId}")
    public String deActiveLoan(@PathVariable Long loanId) throws LoanNotFoundException {
        loanDbService.deActiveLoan(loanId);
        return "Loan is deActivated";
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoanBaseDto> updateLoan(@RequestBody LoanBaseDto loanBaseDto)
            throws AccountNotFoundException, LoanNotFoundException {
        Loan loan = loanMapper.mapToLoan(loanBaseDto);
        return ResponseEntity.ok(loanMapper.mapToLoanBaseDto(loanDbService.updateLoan(loan)));
    }
}
