package com.greg.banking_app.service;

import com.greg.banking_app.domain.Account;
import com.greg.banking_app.exception.AccountNotFoundException;
import com.greg.banking_app.exception.UserNotFoundException;
import com.greg.banking_app.repository.AccountRepository;
import com.greg.banking_app.repository.UserRepository;
import com.greg.banking_app.utils.AccountNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountDbService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountNumberGenerator numberGenerator;

    public List<Account> getUserAccounts(final Long userId) throws UserNotFoundException {
        if(userRepository.existsById(userId)) {
            return accountRepository.findByUser_UserId(userId);
        } else {
            throw new UserNotFoundException();
        }
    }

    public Account getUserAccount(final Long userId, final Long accountId) throws AccountNotFoundException, UserNotFoundException {
        if(userRepository.existsById(userId)) {
            if(accountRepository.existsById(accountId)) {
                return accountRepository.findByUser_UserIdAndAccountId(userId, accountId);
            } else {
                throw new AccountNotFoundException();
            }
        } else {
            throw new UserNotFoundException();
        }
    }

    public Account createAccount(final Account account) {
        Account processedAccount = accountRepository.save(account);
        Long id = processedAccount.getAccountId();
        String newAccountNumber = numberGenerator.generateStandardNumber(id);
        processedAccount.setAccountNumber(newAccountNumber);
        return accountRepository.save(processedAccount);
    }

    public void deActiveAccount(final Long id) throws AccountNotFoundException {
        if(accountRepository.existsById(id)) {
            Account currentAccount = accountRepository.findById(id).orElseThrow(AccountNotFoundException::new);
            currentAccount.setActive(false);
            accountRepository.save(currentAccount);
        } else {
            throw new AccountNotFoundException();
        }
    }
}
