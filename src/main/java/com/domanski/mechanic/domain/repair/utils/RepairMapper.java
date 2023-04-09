package com.domanski.mechanic.domain.repair.utils;

import com.domanski.mechanic.domain.repair.dto.PartResponse;
import com.domanski.mechanic.domain.repair.dto.RepairResponse;
import com.domanski.mechanic.domain.repair.model.Repair;
import com.domanski.mechanic.domain.repair.model.RepairPart;

public class RepairMapper {

    public static RepairResponse mapFromRepair(Repair repair) {
        return RepairResponse.builder()
                .id(repair.getId())
                .description(repair.getDescription())
                .date(repair.getDate())
                .parts(repair.getRepairParts().stream()
                        .map(RepairMapper::mapFromRepairPart)
                        .toList())
                .workTime(repair.getWorkTime())
                .repairCost(repair.getRepairCost())
                .repairStatus(repair.getRepairStatus())
                .build();
    }

    private static PartResponse mapFromRepairPart(RepairPart repairPart) {
        return PartResponse.builder()
                .name(repairPart.getPart().getName())
                .price(repairPart.getPart().getPrice())
                .quantity(repairPart.getQuantity())
                .build();
    }
}
