package com.domanski.mechanic.domain.repair.dto;

import com.domanski.mechanic.domain.repair.model.RepairStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
public record RepairResponse(Long id,
                             String description,
                             LocalDate date,
                             List<UsedPartResponse> parts,
                             Double workTime,
                             BigDecimal repairCost,
                             RepairStatus repairStatus) {
}
