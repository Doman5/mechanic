package com.domanski.mechanic.domain.repair.dto;

import lombok.Builder;

@Builder
public record UsedPartRequest(Long partId,
                              Long quantity) {
}
