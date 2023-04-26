package com.domanski.mechanic.domain.repair.service;

import com.domanski.mechanic.domain.repair.model.Repair;
import com.domanski.mechanic.domain.repair.model.RepairStatus;
import com.domanski.mechanic.domain.repair.repository.RepairRepository;
import com.domanski.mechanic.domain.repair.service.utils.RepairDateGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
public class RepairDateGeneratorService {

    private final RepairRepository repairRepository;
    private final RepairDateGenerator repairDateGenerator;

    public void generateDate() {
        log.info("start generate date for repairs");
        List<Repair> repairsWithoutDate = repairRepository.findAllByRepairStatus(RepairStatus.DATE_NOT_SPECIFIED);
        repairsWithoutDate
                .forEach(repairDateGenerator::generateDateAndUpdateRepairInformation);
    }
}
