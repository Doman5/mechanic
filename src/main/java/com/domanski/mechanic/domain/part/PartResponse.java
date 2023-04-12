package com.domanski.mechanic.domain.part;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PartResponse(Long id,
                           String name,
                           BigDecimal price) {
}
