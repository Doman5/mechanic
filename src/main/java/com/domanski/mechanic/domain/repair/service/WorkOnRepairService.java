package com.domanski.mechanic.domain.repair.service;

import com.domanski.mechanic.domain.repair.dto.PartsAndWorkTimeRequest;
import com.domanski.mechanic.domain.repair.dto.RepairResponse;
import com.domanski.mechanic.domain.repair.error.RepairNoFoundException;
import com.domanski.mechanic.domain.repair.model.Repair;
import com.domanski.mechanic.domain.repair.repository.RepairRepository;
import com.domanski.mechanic.domain.repair.service.utils.RepairCostCalculator;
import com.domanski.mechanic.domain.repair.service.utils.RepairMapper;
import com.domanski.mechanic.domain.repair.service.utils.RepairUsedPartManager;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import static com.domanski.mechanic.domain.repair.service.utils.RepairStatusChanger.changeRepairStatusToFinished;
import static com.domanski.mechanic.domain.repair.service.utils.RepairStatusChanger.changeRepairStatusToWorkInProgressIfWasNotChangedBefore;

@RequiredArgsConstructor
public class WorkOnRepairService {

    private final RepairRepository repairRepository;
    private final RepairUsedPartManager repairUsedPartManager;
    private final RepairCostCalculator repairCostCalculator;

    public RepairResponse doRepairWithParts(Long id, PartsAndWorkTimeRequest partsAndWorkTimeRequest) {
        Repair repair = repairRepository.findById(id)
                .orElseThrow(() -> new RepairNoFoundException("Repair with %d no found".formatted(id)));
        repairUsedPartManager.addPartsToRepair(repair, partsAndWorkTimeRequest);
        repair.setWorkTime(repair.getWorkTime() + partsAndWorkTimeRequest.workiTime());
        BigDecimal priceForRepair = repairCostCalculator.calculatePriceForRepair(repair);
        repair.setRepairCost(priceForRepair);
        changeRepairStatusToWorkInProgressIfWasNotChangedBefore(repair);
        Repair savedRepair = repairRepository.save(repair);
        return RepairMapper.mapFromRepair(savedRepair);
    }

    public RepairResponse finishRepair(Long id) {
        Repair repair = repairRepository.findById(id)
                .orElseThrow(() -> new RepairNoFoundException("Repair with id %d no found".formatted(id)));
        changeRepairStatusToFinished(repair);
        Repair changedRepair = repairRepository.save(repair);
        return RepairMapper.mapFromRepair(changedRepair);
    }
}
