package com.domanski.mechanic.domain.repair.service.utils;

import com.domanski.mechanic.domain.repair.model.Repair;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public
class RepairCostCalculator {

    private final double priceForOneWorkHour;

    public BigDecimal calculatePriceForRepair(Repair repair) {
        BigDecimal costForWorkTime = calculateCostForWorkTime(repair);
        BigDecimal costForParts = calculateCostForParts(repair);
        return costForParts.add(costForWorkTime);
    }

    private BigDecimal calculateCostForWorkTime(Repair repair) {
        return BigDecimal.valueOf(repair.getWorkTime() * priceForOneWorkHour);
    }

    private BigDecimal calculateCostForParts(Repair repair) {
        return repair.getRepairParts().stream()
                .map(repairPart -> repairPart.getPart().getPrice().multiply(BigDecimal.valueOf(repairPart.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}
