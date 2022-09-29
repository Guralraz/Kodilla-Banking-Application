package com.greg.banking_app.controller;

import com.greg.banking_app.domain.Account;
import com.greg.banking_app.dto.account.AccountBaseDto;
import com.greg.banking_app.dto.account.AccountCreateDto;
import com.greg.banking_app.exception.AccountNotFoundException;
import com.greg.banking_app.exception.UserNotFoundException;
import com.greg.banking_app.mapper.AccountMapper;
import com.greg.banking_app.service.AccountDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountDbService accountDbService;
    private final AccountMapper accountMapper;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountBaseDto>> getAccounts(@PathVariable Long userId) throws UserNotFoundException {
        return ResponseEntity.ok(accountMapper.mapToAccountBaseDtoList(accountDbService.getUserAccounts(userId)));
    }

    @GetMapping("{userId}/{accountId}")
    public ResponseEntity<AccountBaseDto> getAccount(@PathVariable Long userId, @PathVariable Long accountId) throws UserNotFoundException, AccountNotFoundException {
        return ResponseEntity.ok(accountMapper.mapToAccountBaseDto(accountDbService.getUserAccount(userId, accountId)));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountBaseDto> createAccount(@RequestBody AccountCreateDto accountCreateDto) throws UserNotFoundException {
        Account account = accountMapper.mapToCreateAccount(accountCreateDto);
        return ResponseEntity.ok(accountMapper.mapToAccountBaseDto(accountDbService.createAccount(account)));
    }

    @DeleteMapping("{accountId}")
    public String deActiveAccount(@PathVariable Long accountId) throws AccountNotFoundException {
        accountDbService.deActiveAccount(accountId);
        return "Account is deActivated";
    }
}
