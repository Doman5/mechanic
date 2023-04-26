package com.domanski.mechanic.domain.repair.service.utils;

import com.domanski.mechanic.domain.repair.dto.CreateRepairRequest;
import com.domanski.mechanic.domain.repair.dto.RepairReportResponse;
import com.domanski.mechanic.domain.repair.dto.RepairResponse;
import com.domanski.mechanic.domain.repair.model.Repair;
import com.domanski.mechanic.domain.repair.model.RepairStatus;

import java.math.BigDecimal;
import java.util.Collections;

public class RepairCreateUtils {

    public static RepairReportResponse createNewRepairReportResponse(Repair repair) {
        return RepairReportResponse.builder()
                .message("Naprawa została zgłoszona")
                .repair(RepairResponse.builder()
                        .id(repair.getId())
                        .repairStatus(repair.getRepairStatus())
                        .description(repair.getDescription())
                        .build())
                .build();
    }

    public static Repair createNewRepair(CreateRepairRequest createRepairRequest, Long userId) {
        return Repair.builder()
                .description(createRepairRequest.description())
                .userId(userId)
                .repairStatus(RepairStatus.DATE_NOT_SPECIFIED)
                .workTime(0.0)
                .repairParts(Collections.emptyList())
                .repairCost(BigDecimal.ZERO)
                .build();
    }
}
