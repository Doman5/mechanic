package com.domanski.mechanic.domain.repair;

import com.domanski.mechanic.domain.loginandregister.LoginAndRegisterFacade;
import com.domanski.mechanic.domain.repair.dto.CreateRepairRequest;
import com.domanski.mechanic.domain.repair.dto.PartsAndWorkTimeRequest;
import com.domanski.mechanic.domain.repair.dto.RepairReportResponse;
import com.domanski.mechanic.domain.repair.dto.RepairResponse;
import com.domanski.mechanic.domain.repair.error.RepairNoFoundException;
import com.domanski.mechanic.domain.repair.model.Repair;
import com.domanski.mechanic.domain.repair.model.RepairStatus;
import com.domanski.mechanic.domain.repair.repository.RepairRepository;
import com.domanski.mechanic.domain.repair.utils.RepairCostCalculator;
import com.domanski.mechanic.domain.repair.utils.RepairDateGenerator;
import com.domanski.mechanic.domain.repair.utils.RepairMapper;
import com.domanski.mechanic.domain.repair.utils.RepairUsedPartManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

import static com.domanski.mechanic.domain.repair.utils.RepairCreateUtils.createNewRepair;
import static com.domanski.mechanic.domain.repair.utils.RepairCreateUtils.createNewRepairReportResponse;
import static com.domanski.mechanic.domain.repair.utils.RepairStatusChanger.changeRepairStatusToFinished;
import static com.domanski.mechanic.domain.repair.utils.RepairStatusChanger.changeRepairStatusToWorkInProgressIfWasNotChangedBefore;

@RequiredArgsConstructor
@Slf4j
public class RepairFacade {

    private final RepairRepository repairRepository;
    private final RepairCostCalculator repairCostCalculator;
    private final RepairUsedPartManager repairUsedPartManager;
    private final RepairDateGenerator repairDateGenerator;
    private final LoginAndRegisterFacade loginAndRegisterFacade;

    public RepairResponse getRepair(Long id) {
        return repairRepository.findById(id)
                .map(RepairMapper::mapFromRepair)
                .orElseThrow(() -> new RepairNoFoundException("Repair with id %d no found".formatted(id)));
    }

    public List<RepairResponse> getAllAwaitingRepairs() {
        return repairRepository.findAllByRepairStatus(RepairStatus.AWAITING).stream()
                .map(RepairMapper::mapFromRepair)
                .toList();
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

    public List<RepairResponse> getUserRepairs(String username) {
        Long userId = loginAndRegisterFacade.findByUsername(username).getId();
        return repairRepository.findAllByUserId(userId).stream()
                .map(RepairMapper::mapFromRepair)
                .toList();
    }

    public RepairReportResponse reportRepair(CreateRepairRequest repair, String username) {
        Long userId = loginAndRegisterFacade.findByUsername(username).getId();
        Repair repairToSave = createNewRepair(repair, userId);
        Repair savedRepair = repairRepository.save(repairToSave);
        return createNewRepairReportResponse(savedRepair);
    }

    public void generateAndSetRepairsDates() {
        log.info("start generate date for repairs");
        List<Repair> repairsWithoutDate = repairRepository.findAllByRepairStatus(RepairStatus.DATE_NOT_SPECIFIED);
        repairsWithoutDate
                .forEach(repairDateGenerator::generateDateAndUpdateRepairInformation);

    }

    public RepairResponse finishRepair(Long id) {
        Repair repair = repairRepository.findById(id)
                .orElseThrow(() -> new RepairNoFoundException("Repair with id %d no found".formatted(id)));
        changeRepairStatusToFinished(repair);
        Repair changedRepair = repairRepository.save(repair);
        return RepairMapper.mapFromRepair(changedRepair);
    }
}
