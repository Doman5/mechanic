package com.domanski.mechanic.domain.part.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PartResponse(Long id,
                           String name,
                           BigDecimal price) {
}
