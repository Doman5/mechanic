package com.domanski.mechanic.domain.repair.dto;

import lombok.Builder;

@Builder
public record PartRequest(Long partId,
                          Long quantity) {
}
