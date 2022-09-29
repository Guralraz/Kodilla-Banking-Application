package com.greg.banking_app.mapper;

import com.greg.banking_app.domain.Installment;
import com.greg.banking_app.domain.Loan;
import com.greg.banking_app.dto.installment.InstallmentDto;
import com.greg.banking_app.exception.LoanNotFoundException;
import com.greg.banking_app.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstallmentMapper {

    @Autowired
    private LoanRepository loanRepository;

    public Installment mapToInstallment(final InstallmentDto installmentDto) throws LoanNotFoundException {
        Loan currentLoan = loanRepository.findById(installmentDto.getLoanId()).orElseThrow(LoanNotFoundException::new);
        return new Installment(
                installmentDto.getInstallmentId(),
                installmentDto.getInstallmentValue(),
                installmentDto.getLeftToPay(),
                installmentDto.getDueDate(),
                installmentDto.getPaymentDate(),
                installmentDto.isPaid(),
                currentLoan
        );
    }

    public InstallmentDto mapToInstallmentDto(final Installment installment) {
        return new InstallmentDto(
                installment.getInstallmentId(),
                installment.getInstallmentValue(),
                installment.getLeftToPay(),
                installment.getDueDate(),
                installment.getPaymentDate(),
                installment.isPaid(),
                installment.getLoan().getLoanId()
        );
    }

    public List<InstallmentDto> mapToInstallmentDtoList(final List<Installment> list) {
        return list.stream()
                .map(this::mapToInstallmentDto)
                .collect(Collectors.toList());
    }
}
