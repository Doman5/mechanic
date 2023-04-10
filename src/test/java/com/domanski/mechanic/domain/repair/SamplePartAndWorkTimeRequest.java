package com.domanski.mechanic.domain.repair;

import com.domanski.mechanic.domain.repair.dto.PartRequest;
import com.domanski.mechanic.domain.repair.dto.PartsAndWorkTimeRequest;

import java.util.List;

public interface SamplePartAndWorkTimeRequest {

    default PartsAndWorkTimeRequest createPartsAndWorkTimeRequest() {
        return PartsAndWorkTimeRequest.builder()
                .parts(List.of(
                        PartRequest.builder()
                                .partId(1L)
                                .quantity(1L)
                                .build(),
                        PartRequest.builder()
                                .partId(2L)
                                .quantity(10L)
                                .build()))
                .workiTime(4.0)
                .build();
    }

   default PartsAndWorkTimeRequest createPartsAndWorkTimeRequestWithEarlierUsedPart() {
        return PartsAndWorkTimeRequest.builder()
                .parts(List.of(
                        PartRequest.builder()
                                .partId(1L)
                                .quantity(1L)
                                .build()))
                .workiTime(1.0)
                .build();
    }


    default PartsAndWorkTimeRequest createPartsAndWorkTimeRequestWithNeverUsedPart() {
        return PartsAndWorkTimeRequest.builder()
                .parts(List.of(
                        PartRequest.builder()
                                .partId(3L)
                                .quantity(1L)
                                .build()))
                .workiTime(1.0)
                .build();
    }
}
