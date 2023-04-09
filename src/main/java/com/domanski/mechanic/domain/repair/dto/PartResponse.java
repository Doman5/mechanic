package com.domanski.mechanic.domain.repair.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PartResponse(
        String name,
        BigDecimal price,
        Long quantity) {
}
