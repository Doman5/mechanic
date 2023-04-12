package com.domanski.mechanic.domain.part;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PartRequest(String name,
                          BigDecimal price) {
}
