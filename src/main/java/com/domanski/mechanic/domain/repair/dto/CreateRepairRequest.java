package com.domanski.mechanic.domain.repair.dto;

import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
public record CreateRepairRequest(
        @NotBlank
        @NotNull
        String description,
        @NotNull
        Long userId) {
}
