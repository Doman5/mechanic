package com.domanski.mechanic.domain.repair.dto;

import lombok.Builder;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
public record PartsAndWorkTimeRequest(
        @NotEmpty
        List<PartRequest> parts,
        @NotNull
        @DecimalMin(value = "0.1")
        Double workiTime) {
}
