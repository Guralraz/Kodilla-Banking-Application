package com.greg.banking_app.scheduler;

import com.greg.banking_app.service.NBPClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NBPClientScheduler {

    private final NBPClientService nbpClientService;

    @Scheduled(cron = "0 0 9 * * 1-5")
    public void getValuesFromNBP() {
        nbpClientService.fetchTableCFromNbp();
    }
}
