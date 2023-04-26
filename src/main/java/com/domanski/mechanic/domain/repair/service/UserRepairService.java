package com.domanski.mechanic.domain.repair.service;

import com.domanski.mechanic.domain.loginandregister.LoginAndRegisterFacade;
import com.domanski.mechanic.domain.repair.dto.CreateRepairRequest;
import com.domanski.mechanic.domain.repair.dto.RepairReportResponse;
import com.domanski.mechanic.domain.repair.dto.RepairResponse;
import com.domanski.mechanic.domain.repair.model.Repair;
import com.domanski.mechanic.domain.repair.repository.RepairRepository;
import com.domanski.mechanic.domain.repair.service.utils.RepairMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.domanski.mechanic.domain.repair.service.utils.RepairCreateUtils.createNewRepair;
import static com.domanski.mechanic.domain.repair.service.utils.RepairCreateUtils.createNewRepairReportResponse;

@RequiredArgsConstructor
public class UserRepairService {

    private final LoginAndRegisterFacade loginAndRegisterFacade;
    private final RepairRepository repairRepository;

    public List<RepairResponse> getUserRepairs(String username) {
        Long userId = loginAndRegisterFacade.findByUsername(username).getId();
        return repairRepository.findAllByUserId(userId).stream()
                .map(RepairMapper::mapFromRepair)
                .toList();
    }

    public RepairReportResponse reportRepair(CreateRepairRequest repair, String username) {
        Long userId = loginAndRegisterFacade.findByUsername(username).getId();
        Repair savedRepair = repairRepository.save(
                createNewRepair(repair, userId)
        );
        return createNewRepairReportResponse(savedRepair);
    }
}
