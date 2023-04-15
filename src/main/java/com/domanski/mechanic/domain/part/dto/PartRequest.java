package com.domanski.mechanic.domain.part.dto;

import lombok.Builder;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
public record PartRequest(
        @NotNull(message = "{part.name.not.null}")
        @NotBlank(message = "{part.name.not.blank}")
        String name,
        @NotNull(message = "{part.price.not.null}")
        @DecimalMin(value = "0.1", message = "{part.price.min.value} + 0.1")
        BigDecimal price) {
}
