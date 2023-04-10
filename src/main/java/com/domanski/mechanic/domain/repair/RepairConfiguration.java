package com.domanski.mechanic.domain.repair;

import com.domanski.mechanic.domain.repair.repository.PartRepository;
import com.domanski.mechanic.domain.repair.repository.RepairPartRepository;
import com.domanski.mechanic.domain.repair.repository.RepairRepository;
import com.domanski.mechanic.domain.repair.utils.RepairCostCalculator;
import com.domanski.mechanic.domain.repair.utils.RepairDateGenerator;
import com.domanski.mechanic.domain.repair.utils.RepairUsedPartManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.LocalDate;

@Configuration
public class RepairConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public RepairCostCalculator repairCostCalculator(@Value("${app.repair.priceForOneWorkHour}") double priceForOneWorkHour) {
        return new RepairCostCalculator(priceForOneWorkHour);
    }

    @Bean
    public RepairUsedPartManager repairUsedPartManager(RepairPartRepository repairPartRepository, PartRepository partRepository) {
        return new RepairUsedPartManager(repairPartRepository, partRepository);
    }

    @Bean
    public RepairDateGenerator repairDateGenerator(RepairRepository repairRepository, @Value("${app.repair.maximumRepairPerDay}") Long maximumRepairPerDay, Clock clock) {
        return new RepairDateGenerator(repairRepository, maximumRepairPerDay, LocalDate.now(clock));
    }

    @Bean
    public RepairFacade repairFacade(RepairRepository repairRepository,
                                     RepairCostCalculator repairCostCalculator,
                                     RepairUsedPartManager repairUsedPartManager,
                                     RepairDateGenerator repairDateGenerator) {
        return new RepairFacade(repairRepository, repairCostCalculator, repairUsedPartManager, repairDateGenerator);
    }
}
