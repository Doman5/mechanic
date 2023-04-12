package com.domanski.mechanic.domain.part;

import com.domanski.mechanic.domain.common.Part;

public class PartMapper {
    public static PartResponse mapFromPart(Part part) {
        return PartResponse.builder()
                .id(part.getId())
                .name(part.getName())
                .price(part.getPrice())
                .build();
    }

    public static Part mapFromPartRequest(PartRequest partRequest) {
        return Part.builder()
                .name(partRequest.name())
                .price(partRequest.price())
                .build();
    }
}
