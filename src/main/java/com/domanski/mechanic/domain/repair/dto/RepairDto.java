package com.domanski.mechanic.domain.repair.dto;

import com.domanski.mechanic.domain.repair.RepairStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
public record RepairDto(Long id,
                        String description,
                        LocalDate date,
                        List<PartResponse> parts,
                        Double workTime,
                        BigDecimal repairCost,
                        RepairStatus repairStatus) {
}
