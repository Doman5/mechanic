package com.domanski.mechanic.domain.part;

import com.domanski.mechanic.domain.common.PartRepository;
import com.domanski.mechanic.domain.repair.error.PartNoFoundException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PartFacadeTest {

    private final PartRepository partRepository = new PartRepositoryInMemoryImpl();
    private final PartFacade partFacade = new PartFacade(partRepository);

    @Test
    public void should_add_new_part() {
        //given
        PartRequest partRequest = PartRequest.builder()
                .name("Part")
                .price(BigDecimal.valueOf(10))
                .build();
        //when
        PartResponse partResponse = partFacade.addPart(partRequest);
        //then
        assertAll(
                () -> assertThat(partResponse.id()).isEqualTo(1L),
                () -> assertThat(partResponse.name()).isEqualTo("Part"),
                () -> assertThat(partResponse.price()).isEqualTo(BigDecimal.valueOf(10))
        );
    }

    @Test
    public void should_return_one_part() {
        //given
        saveOnePartInDatabase();
        Long partId = 1L;
        //when
        PartResponse part = partFacade.getPart(partId);
        //then
        assertAll(
                () -> assertThat(part.id()).isEqualTo(1L),
                () -> assertThat(part.name()).isEqualTo("Part"),
                () -> assertThat(part.price()).isEqualTo(BigDecimal.valueOf(1))
        );
    }


    @Test
    public void should_return_list_of_parts() {
        //given
        saveThreePartsInDatabase();
        //when
        List<PartResponse> parts = partFacade.getParts();
        //then
        assertAll(
                () -> assertThat(parts).hasSize(3),
                () -> assertThat(parts.get(0).name()).isEqualTo("Part1"),
                () -> assertThat(parts.get(1).name()).isEqualTo("Part2"),
                () -> assertThat(parts.get(2).name()).isEqualTo("Part3"),
                () -> assertThat(parts.get(0).price()).isEqualTo(BigDecimal.valueOf(1)),
                () -> assertThat(parts.get(1).price()).isEqualTo(BigDecimal.valueOf(10)),
                () -> assertThat(parts.get(2).price()).isEqualTo(BigDecimal.valueOf(100))
        );
    }

    @Test
    public void should_update_part() {
        //given
        saveOnePartInDatabase();
        Long partId = 1L;
        PartRequest partRequest = PartRequest.builder()
                .name("New part name")
                .price(BigDecimal.valueOf(2))
                .build();
        //when
        PartResponse part = partFacade.updatePart(partId, partRequest);
        //then
        assertAll(
                () -> assertThat(part.id()).isEqualTo(1L),
                () -> assertThat(part.name()).isEqualTo("New part name"),
                () -> assertThat(part.price()).isEqualTo(BigDecimal.valueOf(2))
        );
    }

    @Test
    public void should_delete_part_and_when_checking_throw_part_no_found_exception() {
        //given
        saveOnePartInDatabase();
        Long partId = 1L;
        //when && then
        partFacade.deletePart(partId);
        assertThrows(PartNoFoundException.class, () -> partFacade.getPart(1L), "Part with id 1 no found");
    }

    private void saveOnePartInDatabase() {
        PartRequest partRequest1 = PartRequest.builder()
                .name("Part")
                .price(BigDecimal.valueOf(1))
                .build();
        partFacade.addPart(partRequest1);
    }

    private void saveThreePartsInDatabase() {
        PartRequest partRequest1 = PartRequest.builder()
                .name("Part1")
                .price(BigDecimal.valueOf(1))
                .build();
        partFacade.addPart(partRequest1);
        PartRequest partRequest2 = PartRequest.builder()
                .name("Part2")
                .price(BigDecimal.valueOf(10))
                .build();
        partFacade.addPart(partRequest2);
        PartRequest partRequest3 = PartRequest.builder()
                .name("Part3")
                .price(BigDecimal.valueOf(100))
                .build();
        partFacade.addPart(partRequest3);
    }
}