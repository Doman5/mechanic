package com.domanski.mechanic.domain.repair.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record UsedPartResponse(
        String name,
        BigDecimal price,
        Long quantity) {
}
