package com.greg.banking_app.mapper;

import com.greg.banking_app.domain.Account;
import com.greg.banking_app.domain.User;
import com.greg.banking_app.dto.account.AccountBaseDto;
import com.greg.banking_app.dto.account.AccountCreateDto;
import com.greg.banking_app.exception.UserNotFoundException;
import com.greg.banking_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountMapper {

    @Autowired
    private UserRepository userRepository;

    public AccountBaseDto mapToAccountBaseDto(final Account account) {
        return new AccountBaseDto(
                account.getAccountId(),
                account.getAccountNumber(),
                account.getOpenDate(),
                account.getCloseDate(),
                account.getPresentValue(),
                account.getCurrencySymbol(),
                account.isActive(),
                account.getUser().getUserId()
        );
    }

    public List<AccountBaseDto> mapToAccountBaseDtoList(final List<Account> accounts) {
        return accounts.stream()
                .map(this::mapToAccountBaseDto)
                .collect(Collectors.toList());
    }

    public Account mapToCreateAccount(final AccountCreateDto accountCreateDto) throws UserNotFoundException {
        User user = userRepository.findById(accountCreateDto.getUserId()).orElseThrow(UserNotFoundException::new);
        return new Account(accountCreateDto.getCurrencySymbol(), user);
    }
}
