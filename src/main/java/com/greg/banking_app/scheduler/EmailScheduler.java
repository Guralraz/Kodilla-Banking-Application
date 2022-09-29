package com.greg.banking_app.scheduler;

import com.greg.banking_app.service.overdue_service.OverdueService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailScheduler {

    private final OverdueService overdueService;

    @Scheduled(cron = "0 0 9 * * *")
    public void informAboutOverdues() {
        overdueService.informAboutOverdues();
    }
}
