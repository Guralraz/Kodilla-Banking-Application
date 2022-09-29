package com.greg.banking_app.controller;

import com.greg.banking_app.domain.Installment;
import com.greg.banking_app.dto.installment.InstallmentDto;
import com.greg.banking_app.exception.InstallmentNotFoundException;
import com.greg.banking_app.exception.LoanNotFoundException;
import com.greg.banking_app.mapper.InstallmentMapper;
import com.greg.banking_app.service.InstallmentDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/installments")
@RequiredArgsConstructor
public class InstallmentController {

    private final InstallmentDbService installmentDbService;
    private final InstallmentMapper installmentMapper;

    @GetMapping("loan/{loanId}")
    public ResponseEntity<List<InstallmentDto>> getInstallments(@PathVariable Long loanId) throws LoanNotFoundException {
        return ResponseEntity.ok(installmentMapper.mapToInstallmentDtoList(installmentDbService.getLoanInstallments(loanId)));
    }

    @GetMapping("{installmentId}")
    public ResponseEntity<InstallmentDto> getInstallment(@PathVariable Long installmentId) throws InstallmentNotFoundException {
        return ResponseEntity.ok(installmentMapper.mapToInstallmentDto(installmentDbService.getInstallment(installmentId)));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InstallmentDto> updateInstallment(@RequestBody InstallmentDto installmentDto) throws LoanNotFoundException, InstallmentNotFoundException {
        Installment installment = installmentMapper.mapToInstallment(installmentDto);
        return ResponseEntity.ok(installmentMapper.mapToInstallmentDto(installmentDbService.updateInstallment(installment)));
    }
}
