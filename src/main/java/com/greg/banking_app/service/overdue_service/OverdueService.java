package com.greg.banking_app.service.overdue_service;

import com.greg.banking_app.domain.Mail;
import com.greg.banking_app.domain.User;
import com.greg.banking_app.service.SimpleEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OverdueService {

    private final OverdueVerifier overdueVerifier;
    private final SimpleEmailService emailService;
    private final static String SUBJECT = "Arrears in paying installments";
    private final static String MESSAGE = "You are in arrears in paying installments. Please pay off as soon as possible.";


    public void informAboutOverdues() {
        List<User> users = overdueVerifier.getUsersWithOverdues();
        if(!users.isEmpty()) {
            users.forEach(
                    user -> emailService.send(new Mail(
                    user.getEmail(),
                    SUBJECT,
                    MESSAGE))
            );
        } else {
            emailService.send(new Mail(
                    "appUser@com", //App user email address to implement into Application.properties
                    SUBJECT,
                    "There are no overdues payments"
            ));
        }
    }
}
