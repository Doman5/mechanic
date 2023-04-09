package com.domanski.mechanic.domain.repair.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PartsAndWorkTimeRequest(List<PartRequest> parts,
                                      Double workiTime) {
}
