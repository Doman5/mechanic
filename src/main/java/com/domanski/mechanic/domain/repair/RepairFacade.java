package com.domanski.mechanic.domain.repair;
import com.domanski.mechanic.domain.repair.dto.CreateRepairRequest;
import com.domanski.mechanic.domain.repair.dto.PartsAndWorkTimeRequest;
import com.domanski.mechanic.domain.repair.dto.RepairReportResponse;
import com.domanski.mechanic.domain.repair.dto.RepairResponse;
import com.domanski.mechanic.domain.repair.model.RepairStatus;
import com.domanski.mechanic.domain.repair.service.RepairDateGeneratorService;
import com.domanski.mechanic.domain.repair.service.RepairService;
import com.domanski.mechanic.domain.repair.service.UserRepairService;
import com.domanski.mechanic.domain.repair.service.WorkOnRepairService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class RepairFacade {

    private final RepairService repairService;
    private final WorkOnRepairService workOnRepairService;
    private final RepairDateGeneratorService repairDateGeneratorService;
    private final UserRepairService userRepairService;


    public RepairResponse getRepair(Long id) {
        return repairService.getRepair(id);
    }

    public List<RepairResponse> getAllRepairsByStatus(RepairStatus repairStatus) {
        return repairService.getAllRepairsByStatus(repairStatus);
    }

    public RepairResponse doRepairWithPartsAndWorkTime(Long id, PartsAndWorkTimeRequest partsAndWorkTimeRequest) {
        return workOnRepairService.doRepairWithParts(id, partsAndWorkTimeRequest);

    }

    public List<RepairResponse> getUserRepairs(String username) {
        return userRepairService.getUserRepairs(username);
    }

    public RepairReportResponse reportRepair(CreateRepairRequest repair, String username) {
        return userRepairService.reportRepair(repair, username);
    }

    public void generateAndSetRepairsDates() {
        repairDateGeneratorService.generateDate();
    }

    public RepairResponse finishRepair(Long id) {
        return workOnRepairService.finishRepair(id);

    }
}
