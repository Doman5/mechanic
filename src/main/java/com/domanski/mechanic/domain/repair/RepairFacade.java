package com.domanski.mechanic.domain.repair;

import com.domanski.mechanic.domain.repair.dto.CreateRepairRequest;
import com.domanski.mechanic.domain.repair.dto.PartsAndWorkTimeRequest;
import com.domanski.mechanic.domain.repair.dto.RepairResponse;
import com.domanski.mechanic.domain.repair.error.RepairNoFoundException;
import com.domanski.mechanic.domain.repair.model.Repair;
import com.domanski.mechanic.domain.repair.model.RepairStatus;
import com.domanski.mechanic.domain.repair.repository.RepairRepository;
import com.domanski.mechanic.domain.repair.utils.RepairCostCalculator;
import com.domanski.mechanic.domain.repair.utils.RepairMapper;
import com.domanski.mechanic.domain.repair.utils.RepairUsedPartManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.domanski.mechanic.domain.repair.utils.RepairStatusChanger.changeRepairStatusToWorkInProgressIfWasNotChangedBefore;

@RequiredArgsConstructor
@Slf4j
public class RepairFacade {

    private final RepairRepository repairRepository;
    private final RepairCostCalculator repairCostCalculator;
    private final RepairUsedPartManager repairUsedPartManager;

    public RepairResponse getRepair(Long id) {
        return repairRepository.findById(id)
                .map(RepairMapper::mapFromRepair)
                .orElseThrow(() -> new RepairNoFoundException("Repair with partId %d no found".formatted(id)));
    }

    public RepairResponse createRepair(CreateRepairRequest createRepairRequest) {
        Repair repairToSave = createNewRepair(createRepairRequest);
        Repair savedRepair = repairRepository.save(repairToSave);
        return RepairMapper.mapFromRepair(savedRepair);
    }

    public RepairResponse doRepairWithPartsAndWorkTime(Long id, PartsAndWorkTimeRequest partsAndWorkTimeRequest) {
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

    public List<RepairResponse> getUserRepairs(Long userId) {
        return repairRepository.findAllByUserId(userId).stream()
                .map(RepairMapper::mapFromRepair)
                .toList();
    }

    public long checkRepairQuantityForDate(LocalDate date) {
        return repairRepository.findAllByDate(date).size();
    }

    private static Repair createNewRepair(CreateRepairRequest createRepairRequest) {
        return Repair.builder()
                .description(createRepairRequest.description())
                .userId(createRepairRequest.userId())
                .repairStatus(RepairStatus.DATE_NOT_SPECIFIED)
                .workTime(0.0)
                .repairParts(Collections.emptyList())
                .repairCost(BigDecimal.ZERO)
                .build();
    }

    public List<RepairResponse> getAllRepairWithUndefinedDate() {
        List<Repair> repairs = repairRepository.findAllByRepairStatus(RepairStatus.DATE_NOT_SPECIFIED);
        return repairs.stream()
                .map(RepairMapper::mapFromRepair)
                .toList();
    }
}
