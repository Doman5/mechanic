package com.domanski.mechanic.infrastucture.repair.scheduler;

import com.domanski.mechanic.domain.repair.RepairFacade;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@Slf4j
@Component
public class RepairDateSetterScheduler {

    public static final String START_GENERATE_DATES = "Start generate dates for repairs";
    public static final String END_GENERATED_DATES = "End generated dates for repairs";
    private final RepairFacade repairFacade;

    @Scheduled(fixedDelayString = "${app.repair.scheduler.delay}")
    public void generateAndSetRepairsDates() {
        log.info(START_GENERATE_DATES);
        repairFacade.generateAndSetRepairsDates();
        log.info(END_GENERATED_DATES);
    }
}
