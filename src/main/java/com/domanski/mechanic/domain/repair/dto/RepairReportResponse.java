package com.domanski.mechanic.domain.repair.dto;

import lombok.Builder;

@Builder
public record RepairReportResponse(String message,
                                   RepairResponse repair) {
}
