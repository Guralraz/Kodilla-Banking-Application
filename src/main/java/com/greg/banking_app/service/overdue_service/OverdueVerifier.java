package com.greg.banking_app.service.overdue_service;

import com.greg.banking_app.domain.Installment;
import com.greg.banking_app.domain.User;
import com.greg.banking_app.service.InstallmentDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Service
@RequiredArgsConstructor
public class OverdueVerifier {

    private final InstallmentDbService installmentDbService;

    public List<User> getUsersWithOverdues() {
        return getInstallments().stream()
                .map(installment -> installment.getLoan())
                .map(loan -> loan.getAccount())
                .map(account -> account.getUser())
                .collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparingLong(User::getUserId))),
                        ArrayList::new));
    }

    private List<Installment> getInstallments() {
        return installmentDbService.getAllInstallment().stream()
                .filter(installment -> !installment.isPaid())
                .filter(installment -> installment.getDueDate().isBefore(LocalDate.now().minusDays(1)))
                .collect(Collectors.toList());
    }
}

