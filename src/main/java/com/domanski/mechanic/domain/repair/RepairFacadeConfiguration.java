package com.domanski.mechanic.domain.repair;

import com.domanski.mechanic.domain.common.PartRepository;
import com.domanski.mechanic.domain.loginandregister.LoginAndRegisterFacade;
import com.domanski.mechanic.domain.repair.repository.RepairPartRepository;
import com.domanski.mechanic.domain.repair.repository.RepairRepository;
import com.domanski.mechanic.domain.repair.service.RepairDateGeneratorService;
import com.domanski.mechanic.domain.repair.service.RepairService;
import com.domanski.mechanic.domain.repair.service.UserRepairService;
import com.domanski.mechanic.domain.repair.service.utils.RepairCostCalculator;
import com.domanski.mechanic.domain.repair.service.utils.RepairDateGenerator;
import com.domanski.mechanic.domain.repair.service.utils.RepairUsedPartManager;
import com.domanski.mechanic.domain.repair.service.WorkOnRepairService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class RepairFacadeConfiguration {

    @Bean
    public RepairFacade repairFacade(RepairService repairService,
                                     WorkOnRepairService workOnRepairService,
                                     RepairDateGeneratorService repairDateGeneratorService,
                                     UserRepairService userRepairService) {
        return new RepairFacade(repairService, workOnRepairService, repairDateGeneratorService, userRepairService);
    }

    @Bean
    public RepairService repairService(RepairRepository repairRepository) {
        return new RepairService(repairRepository);
    }

    @Bean
    public WorkOnRepairService workOnRepairService(RepairRepository repairRepository,
                                                   RepairUsedPartManager repairUsedPartManager,
                                                   RepairCostCalculator repairCostCalculator) {
        return new WorkOnRepairService(repairRepository, repairUsedPartManager, repairCostCalculator);
    }

    @Bean
    public RepairDateGeneratorService repairDateGeneratorService(RepairRepository repairRepository,
                                                                 RepairDateGenerator repairDateGenerator) {
        return new RepairDateGeneratorService(repairRepository, repairDateGenerator);
    }

    @Bean UserRepairService userRepairService(LoginAndRegisterFacade loginAndRegisterFacade,
                                              RepairRepository repairRepository) {
        return new UserRepairService(loginAndRegisterFacade, repairRepository);
    }

    @Bean
    public RepairCostCalculator repairCostCalculator(@Value("${app.repair.priceForOneWorkHour}") double priceForOneWorkHour) {
        return new RepairCostCalculator(priceForOneWorkHour);
    }

    @Bean
    public RepairUsedPartManager repairUsedPartManager(RepairPartRepository repairPartRepository,
                                                       PartRepository partRepository) {
        return new RepairUsedPartManager(repairPartRepository, partRepository);
    }

    @Bean
    public RepairDateGenerator repairDateGenerator(RepairRepository repairRepository,
                                                   @Value("${app.repair.maximumRepairPerDay}") Long maximumRepairPerDay,
                                                   Clock clock) {
        return new RepairDateGenerator(repairRepository, maximumRepairPerDay, clock);
    }


}
