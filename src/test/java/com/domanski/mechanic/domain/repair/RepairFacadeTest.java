package com.domanski.mechanic.domain.repair;

import com.domanski.mechanic.domain.repair.dto.CreateRepairRequest;
import com.domanski.mechanic.domain.repair.dto.PartRequest;
import com.domanski.mechanic.domain.repair.dto.RepairResponse;
import com.domanski.mechanic.domain.repair.dto.PartsAndWorkTimeRequest;
import com.domanski.mechanic.domain.repair.error.PartNoFoundException;
import com.domanski.mechanic.domain.repair.error.RepairNoFoundException;
import com.domanski.mechanic.domain.repair.model.Part;
import com.domanski.mechanic.domain.repair.model.Repair;
import com.domanski.mechanic.domain.repair.model.RepairStatus;
import com.domanski.mechanic.domain.repair.repository.PartRepository;
import com.domanski.mechanic.domain.repair.repository.RepairPartRepository;
import com.domanski.mechanic.domain.repair.repository.RepairRepository;
import com.domanski.mechanic.domain.repair.utils.RepairCostCalculator;
import com.domanski.mechanic.domain.repair.utils.RepairUsedPartManager;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RepairFacadeTest {

    private final RepairRepository repairRepository = new RepairRepositoryInMemoryImpl();
    private final PartRepository partRepository = new PartRepositoryInMemoryImpl();
    private final RepairPartRepository repairPartRepository = new RepairPartRepositoryInMemoryImpl();
    private final RepairCostCalculator repairCostCalculator = new RepairCostCalculator(100);
    private final RepairUsedPartManager repairUsedPartManager = new RepairUsedPartManager(repairPartRepository, partRepository);
    private final RepairFacade repairFacade = new RepairFacade(
            repairRepository,
            repairCostCalculator,
            repairUsedPartManager

    );

    @Test
    public void should_return_repair_when_it_exist_in_database() {
        //given
        Long searchingRepairId = 1L;
        saveOneRepair();
        //when
        RepairResponse repairResult = repairFacade.getRepair(searchingRepairId);
        //then
        assertThat(repairResult.id()).isEqualTo(1L);
        assertThat(repairResult.description()).isEqualTo("Simple description");
        assertThat(repairResult.date()).isEqualTo(LocalDate.of(2022, 5, 5));
    }

    @Test
    void should_throw_repair_no_found_exception_when_it_no_exist_in_database() {
        //given
        Long searchingRepairId = 1L;
        //when && then
        assertThrows(RepairNoFoundException.class, () -> repairFacade.getRepair(searchingRepairId), "Repair with partId 1 no found");
    }

    @Test
    void should_create_repair_from_create_repair_request() {
        //given
        CreateRepairRequest createRepairRequest = CreateRepairRequest.builder()
                .description("New Description")
                .userId(1L)
                .build();
        //when
        RepairResponse repairResult = repairFacade.createRepair(createRepairRequest);
        //then
        assertAll(
                () -> assertThat(repairResult.id()).isEqualTo(1L),
                () -> assertThat(repairResult.description()).isEqualTo("New Description"),
                () -> assertThat(repairResult.date()).isNull(),
                () -> assertThat(repairResult.workTime()).isEqualTo(0),
                () -> assertThat(repairResult.repairCost()).isEqualTo(BigDecimal.ZERO),
                () -> assertThat(repairResult.parts()).isEqualTo(Collections.emptyList()),
                () -> assertThat(repairResult.repairStatus()).isEqualTo(RepairStatus.DATE_NOT_SPECIFIED)
        );
    }

    @Test
    public void should_add_parts_and_work_time_to_repair() {
        //given
        Long repairId = 1L;
        createRepair();

        partRepository.save(Part.builder()
                .id(1L)
                .name("part 1")
                .price(BigDecimal.valueOf(100))
                .build());
        partRepository.save(Part.builder()
                .id(2L)
                .name("part 2")
                .price(BigDecimal.valueOf(10))
                .build());

        PartsAndWorkTimeRequest workRepairRequest = PartsAndWorkTimeRequest.builder()
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
        //when
        RepairResponse repairAfterWork = repairFacade.doRepairWithPartsAndWorkTime(repairId, workRepairRequest);
        //then
        assertAll(
                () -> assertThat(repairAfterWork.workTime()).isEqualTo(4),
                () -> assertThat(repairAfterWork.parts()).hasSize(2),
                () -> assertThat(repairAfterWork.repairCost()).isEqualTo(BigDecimal.valueOf(600.0)),
                () -> assertThat(repairAfterWork.repairStatus()).isEqualTo(RepairStatus.WORK_IN_PROGRESS)
        );
    }

    @Test
    public void should_change_quantity_of_earlier_used_part() {
        //given
        Long repairId = 1L;
        createRepair();

        partRepository.save(Part.builder()
                .id(1L)
                .name("part 1")
                .price(BigDecimal.valueOf(100))
                .build());
        partRepository.save(Part.builder()
                .id(2L)
                .name("part 2")
                .price(BigDecimal.valueOf(10))
                .build());

        PartsAndWorkTimeRequest firstPartsAndWorkTimeRequest = PartsAndWorkTimeRequest.builder()
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
        repairFacade.doRepairWithPartsAndWorkTime(repairId, firstPartsAndWorkTimeRequest);

        PartsAndWorkTimeRequest secondPartsAndWorkTimeRequest = PartsAndWorkTimeRequest.builder()
                .parts(List.of(
                        PartRequest.builder()
                                .partId(1L)
                                .quantity(1L)
                                .build()))
                .workiTime(1.0)
                .build();
        //when
        RepairResponse repairResult = repairFacade.doRepairWithPartsAndWorkTime(repairId, secondPartsAndWorkTimeRequest);
        //then
        assertAll(
                () -> assertThat(repairResult.parts().get(0).quantity()).isEqualTo(2),
                () -> assertThat(repairResult.parts().size()).isEqualTo(2),
                () -> assertThat(repairResult.repairCost()).isEqualTo(BigDecimal.valueOf(800.0))
        );
    }

    @Test
    public void should_add_new_part_to_repair() {
        //given
        Long repairId = 1L;
        createRepair();

        partRepository.save(Part.builder()
                .id(1L)
                .name("part 1")
                .price(BigDecimal.valueOf(100))
                .build());
        partRepository.save(Part.builder()
                .id(2L)
                .name("part 2")
                .price(BigDecimal.valueOf(10))
                .build());
        partRepository.save(Part.builder()
                .id(3L)
                .name("part 3")
                .price(BigDecimal.valueOf(200))
                .build());

        PartsAndWorkTimeRequest firstPartsAndWorkTimeRequest = PartsAndWorkTimeRequest.builder()
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
        repairFacade.doRepairWithPartsAndWorkTime(repairId, firstPartsAndWorkTimeRequest);

        PartsAndWorkTimeRequest secondPartsAndWorkTimeRequest = PartsAndWorkTimeRequest.builder()
                .parts(List.of(
                        PartRequest.builder()
                                .partId(3L)
                                .quantity(1L)
                                .build()))
                .workiTime(1.0)
                .build();
        //when
        RepairResponse repairResult = repairFacade.doRepairWithPartsAndWorkTime(repairId, secondPartsAndWorkTimeRequest);
        //then
        assertAll(
                () -> assertThat(repairResult.parts().size()).isEqualTo(3),
                () -> assertThat(repairResult.repairCost()).isEqualTo(BigDecimal.valueOf(900.0))
        );
    }

    @Test
    public void should_throw_part_no_found_exception_when_try_to_use_no_existing_part() {
        //given
        createRepair();
        Long repairId = 1L;
        PartsAndWorkTimeRequest partAndWorkRequest = PartsAndWorkTimeRequest.builder()
                .parts(List.of(
                        PartRequest.builder()
                                .partId(1L)
                                .quantity(10L)
                                .build()))
                .workiTime(2.0)
                .build();
        //when && then
        assertThrows(PartNoFoundException.class, () -> repairFacade.doRepairWithPartsAndWorkTime(repairId, partAndWorkRequest), "Part with id 1 no found");
    }

    @Test
    void should_get_all_repairs_for_user() {
        //given
        Long searchedUserId = 1L;
        saveFourRepairsInDatabase();
        //when
        List<RepairResponse> userRepairsResult = repairFacade.getUserRepairs(searchedUserId);
        //then
        assertAll(
                () -> assertThat(userRepairsResult).hasSize(2),
                () -> assertThat(userRepairsResult.get(0).id()).isEqualTo(1),
                () -> assertThat(userRepairsResult.get(1).id()).isEqualTo(4)
        );
    }

    @Test
    void should_return_empty_list_when_user_has_not_any_repairs() {
        //given
        Long searchedUserId = 1L;
        //when
        List<RepairResponse> userRepairsResult = repairFacade.getUserRepairs(searchedUserId);
        //then
        assertThat(userRepairsResult).isEmpty();
    }

    @Test
    public void should_return_quantity_of_repairs_for_date() {
        //given
        LocalDate checkedDate = LocalDate.of(2022, 5, 5);
        saveSixRepairsWithSpecificDateInDatabase();
        //when
        long result = repairFacade.checkRepairQuantityForDate(checkedDate);
        //then
        assertThat(result).isEqualTo(3);
    }

    @Test
    public void should_return_all_repairs_where_date_was_not_undefined() {
        //given
        saveFourRepairsInDatabase();
        //when
        List<RepairResponse> repairs = repairFacade.getAllRepairWithUndefinedDate();
        //then
        assertThat(repairs.size()).isEqualTo(2);
        assertThat(repairs.get(0).repairStatus()).isEqualTo(RepairStatus.DATE_NOT_SPECIFIED);
    }


    private void saveOneRepair() {
        Repair repair = Repair.builder()
                .userId(1L)
                .description("Simple description")
                .repairStatus(RepairStatus.AWAITING)
                .date(LocalDate.of(2022, 5, 5))
                .repairParts(Collections.emptyList())
                .build();
        repairRepository.save(repair);
    }

    private void saveFourRepairsInDatabase() {
        Repair repair1 = Repair.builder()
                .userId(1L)
                .description("description 1")
                .repairStatus(RepairStatus.FINISHED)
                .date(LocalDate.of(2022, 5, 5))
                .repairParts(Collections.emptyList())
                .build();
        repairRepository.save(repair1);
        Repair repair2 = Repair.builder()
                .userId(3L)
                .description("description 2")
                .repairStatus(RepairStatus.AWAITING)
                .date(LocalDate.of(2022, 5, 7))
                .repairParts(Collections.emptyList())
                .build();
        repairRepository.save(repair2);
        Repair repair3 = Repair.builder()
                .userId(2L)
                .description("description 3")
                .repairStatus(RepairStatus.DATE_NOT_SPECIFIED)
                .repairParts(Collections.emptyList())
                .build();
        repairRepository.save(repair3);
        Repair repair4 = Repair.builder()
                .userId(1L)
                .description("description 4")
                .repairStatus(RepairStatus.DATE_NOT_SPECIFIED)
                .repairParts(Collections.emptyList())
                .build();
        repairRepository.save(repair4);
    }

    private void saveSixRepairsWithSpecificDateInDatabase() {
        Repair repair1 = Repair.builder()
                .userId(1L)
                .description("description 1")
                .repairStatus(RepairStatus.FINISHED)
                .date(LocalDate.of(2022, 5, 5))
                .build();
        repairRepository.save(repair1);
        Repair repair2 = Repair.builder()
                .userId(3L)
                .description("description 2")
                .repairStatus(RepairStatus.AWAITING)
                .date(LocalDate.of(2022, 5, 5))
                .build();
        repairRepository.save(repair2);
        Repair repair3 = Repair.builder()
                .userId(2L)
                .description("description 3")
                .repairStatus(RepairStatus.AWAITING)
                .date(LocalDate.of(2022, 5, 5))
                .build();
        repairRepository.save(repair3);
        Repair repair4 = Repair.builder()
                .userId(1L)
                .description("description 4")
                .repairStatus(RepairStatus.AWAITING)
                .date(LocalDate.of(2022, 5, 7))
                .build();
        repairRepository.save(repair4);
        Repair repair5 = Repair.builder()
                .userId(2L)
                .description("description 5")
                .repairStatus(RepairStatus.AWAITING)
                .date(LocalDate.of(2022, 5, 7))
                .build();
        repairRepository.save(repair5);
        Repair repair6 = Repair.builder()
                .userId(1L)
                .description("description 6")
                .repairStatus(RepairStatus.AWAITING)
                .date(LocalDate.of(2022, 5, 9))
                .build();
        repairRepository.save(repair6);
    }

    private void createRepair() {
        repairFacade.createRepair(CreateRepairRequest.builder()
                .description("description 1")
                .userId(1L)
                .build());
    }
}
