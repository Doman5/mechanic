package com.domanski.mechanic.domain.repair.dto;

import lombok.Builder;

@Builder
public record CreateRepairRequest(String description,
                                  Long userId) {
}
