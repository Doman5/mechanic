package com.domanski.mechanic.domain.repair;

import com.domanski.mechanic.domain.common.Part;
import com.domanski.mechanic.domain.common.PartRepository;
import com.domanski.mechanic.domain.part.PartRepositoryInMemoryImpl;
import com.domanski.mechanic.domain.repair.dto.CreateRepairRequest;
import com.domanski.mechanic.domain.repair.dto.PartsAndWorkTimeRequest;
import com.domanski.mechanic.domain.repair.dto.RepairReportResponse;
import com.domanski.mechanic.domain.repair.dto.RepairResponse;
import com.domanski.mechanic.domain.repair.dto.UsedPartRequest;
import com.domanski.mechanic.domain.repair.error.PartNoFoundException;
import com.domanski.mechanic.domain.repair.error.RepairNoFoundException;
import com.domanski.mechanic.domain.repair.model.RepairPart;
import com.domanski.mechanic.domain.repair.model.RepairStatus;
import com.domanski.mechanic.domain.repair.repository.RepairPartRepository;
import com.domanski.mechanic.domain.repair.repository.RepairRepository;
import com.domanski.mechanic.domain.repair.utils.RepairCostCalculator;
import com.domanski.mechanic.domain.repair.utils.RepairDateGenerator;
import com.domanski.mechanic.domain.repair.utils.RepairUsedPartManager;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RepairFacadeTest implements SamplePartAndWorkTimeRequest {

    private final RepairRepository repairRepository = new RepairRepositoryInMemoryImpl();
    private final PartRepository partRepository = new PartRepositoryInMemoryImpl();
    private final RepairPartRepository repairPartRepository = new RepairPartRepositoryInMemoryImpl();
    private final RepairCostCalculator repairCostCalculator = new RepairCostCalculator(100);
    private final RepairUsedPartManager repairUsedPartManager = new RepairUsedPartManager(repairPartRepository, partRepository);
    private final Long maximumRepairPerDay = 3L;
    private final Clock clock = Clock.fixed(Instant.parse("2022-05-05T12:00:00.00Z"), ZoneOffset.UTC);
    private final RepairDateGenerator repairDateGenerator = new RepairDateGenerator(repairRepository, maximumRepairPerDay, clock);

    private final SampleRepairScenarios sampleRepairScenarios = new SampleRepairScenarios(repairRepository);

    private final RepairFacade repairFacade = new RepairFacade(
            repairRepository,
            repairCostCalculator,
            repairUsedPartManager,
            repairDateGenerator
    );

    @Test
    public void should_return_repair_when_it_exist_in_database() {
        //given
        Long searchingRepairId = 1L;
        sampleRepairScenarios.saveOneRepairInDatabase();
        //when
        RepairResponse repairResult = repairFacade.getRepair(searchingRepairId);
        //then
        assertThat(repairResult.id()).isEqualTo(1L);
        assertThat(repairResult.description()).isEqualTo("Simple description");
        assertThat(repairResult.date()).isEqualTo(LocalDate.of(2022, 5, 5));
    }


    @Test
    public void should_return_all_repairs_saved_in_database() {
        //given
        sampleRepairScenarios.saveFourRepairsInDatabase();
        //when
        List<RepairResponse> allRepairs = repairFacade.getAllRepairs();
        //then
        assertThat(allRepairs).hasSize(4);
    }

    @Test
    public void should_return_empty_list_when_are_not_any_repairs_in_database() {
        //given
        //when
        List<RepairResponse> allRepairs = repairFacade.getAllRepairs();
        //then
        assertThat(allRepairs).hasSize(0);
    }

    @Test
    void should_throw_repair_no_found_exception_when_it_no_exist_in_database() {
        //given
        Long searchingRepairId = 1L;
        //when && then
        assertThrows(RepairNoFoundException.class, () -> repairFacade.getRepair(searchingRepairId), "Repair with partId 1 no found");
    }

    @Test
    public void should_add_parts_and_work_time_to_repair() {
        //given
        Long repairId = 1L;
        sampleRepairScenarios.createNewRepair();
        saveThreePartsInDatabase();

        PartsAndWorkTimeRequest workRepairRequest = createPartsAndWorkTimeRequest();
        //when
        RepairResponse repairAfterWork = repairFacade.doRepairWithPartsAndWorkTime(repairId, workRepairRequest);
        List<RepairPart> repairPartsAfterWork = repairPartRepository.findAll();
        BigDecimal valueOfUsedPart = sumPartsPrices(repairPartsAfterWork);
        //then
        assertAll(
                () -> assertThat(repairPartsAfterWork).hasSize(2),
                () -> assertThat(repairAfterWork.workTime()).isEqualTo(4),
                () -> assertThat(repairAfterWork.repairStatus()).isEqualTo(RepairStatus.WORK_IN_PROGRESS),
                () -> assertThat(repairAfterWork.repairCost()).isEqualTo(BigDecimal.valueOf(400.0)),
                () -> assertThat(valueOfUsedPart).isEqualTo(BigDecimal.valueOf(200))
        );
    }

    @Test
    public void should_change_quantity_of_earlier_used_part() {
        //given
        Long repairId = 1L;
        sampleRepairScenarios.createNewRepair();
        saveThreePartsInDatabase();
        PartsAndWorkTimeRequest firstPartsAndWorkTimeRequest = createPartsAndWorkTimeRequest();
        repairFacade.doRepairWithPartsAndWorkTime(repairId, firstPartsAndWorkTimeRequest);

        PartsAndWorkTimeRequest secondPartsAndWorkTimeRequest = createPartsAndWorkTimeRequestWithEarlierUsedPart();
        //when
        RepairResponse repairResult = repairFacade.doRepairWithPartsAndWorkTime(repairId, secondPartsAndWorkTimeRequest);
        List<RepairPart> repairPartsResult = repairPartRepository.findAll();
        BigDecimal partsValue = sumPartsPrices(repairPartsResult);
        //then
        assertAll(
                () -> assertThat(repairPartsResult.get(0).getQuantity()).isEqualTo(2),
                () -> assertThat(repairPartsResult.size()).isEqualTo(2),
                () -> assertThat(repairResult.repairCost()).isEqualTo(BigDecimal.valueOf(500.0)),
                () -> assertThat(partsValue).isEqualTo(BigDecimal.valueOf(300))
        );
    }

    @Test
    public void should_add_new_part_to_repair() {
        //given
        Long repairId = 1L;
        sampleRepairScenarios.createNewRepair();
        saveThreePartsInDatabase();

        PartsAndWorkTimeRequest firstPartsAndWorkTimeRequest = createPartsAndWorkTimeRequest();
        repairFacade.doRepairWithPartsAndWorkTime(repairId, firstPartsAndWorkTimeRequest);

        PartsAndWorkTimeRequest secondPartsAndWorkTimeRequest = createPartsAndWorkTimeRequestWithNeverUsedPart();
        //when
        RepairResponse repairResult = repairFacade.doRepairWithPartsAndWorkTime(repairId, secondPartsAndWorkTimeRequest);
        List<RepairPart> repairParts = repairPartRepository.findAll();
        BigDecimal partsValue = sumPartsPrices(repairParts);
        //then
        assertAll(
                () -> assertThat(repairParts).hasSize(3),
                () -> assertThat(repairResult.repairCost()).isEqualTo(BigDecimal.valueOf(500.0)),
                () -> assertThat(partsValue).isEqualTo(BigDecimal.valueOf(400))
        );
    }

    @Test
    public void should_throw_part_no_found_exception_when_try_to_use_no_existing_part() {
        //given
        sampleRepairScenarios.createNewRepair();
        Long repairId = 1L;
        PartsAndWorkTimeRequest partAndWorkRequest = PartsAndWorkTimeRequest.builder()
                .parts(List.of(
                        UsedPartRequest.builder()
                                .partId(1L)
                                .quantity(10L)
                                .build()))
                .workiTime(2.0)
                .build();
        //when && then
        assertThrows(PartNoFoundException.class, () -> repairFacade.doRepairWithPartsAndWorkTime(repairId, partAndWorkRequest), "Part with id 1 no found");
    }

    @Test
    public void should_create_new_repair_from_user_request() {
        //given
        CreateRepairRequest createRepairRequest = CreateRepairRequest.builder()
                .userId(1L)
                .description("Description")
                .build();
        //when
        RepairReportResponse repairReportResponse = repairFacade.reportRepair(createRepairRequest);
        //then
        assertAll(
                () -> assertThat(repairReportResponse.message()).isEqualTo("Naprawa została zgłoszona"),
                () -> assertThat(repairReportResponse.repair().id()).isEqualTo(1L),
                () -> assertThat(repairReportResponse.repair().description()).isEqualTo("Description"),
                () -> assertThat(repairReportResponse.repair().repairStatus()).isEqualTo(RepairStatus.DATE_NOT_SPECIFIED)
        );
    }

    @Test
    public void should_generate_dates_for_earlier_added_repairs() {
        //given
        reportRepairsManyTimes(4);
        //when
        repairFacade.generateAndSetRepairsDates();
        //then
        List<RepairResponse> allRepairs = repairFacade.getAllRepairs();
        assertAll(
                () -> assertThat(allRepairs.get(0).date()).isEqualTo(LocalDate.of(2022,5,6)),
                () -> assertThat(allRepairs.get(1).date()).isEqualTo(LocalDate.of(2022,5,6)),
                () -> assertThat(allRepairs.get(2).date()).isEqualTo(LocalDate.of(2022,5,6)),
                () -> assertThat(allRepairs.get(3).date()).isEqualTo(LocalDate.of(2022,5,7))
        );
    }

    @Test
    public void should_generate_dates_for_many_repairs() {
        //given
        reportRepairsManyTimes(10);
        //when
        repairFacade.generateAndSetRepairsDates();
        //then
        List<RepairResponse> allRepairs = repairFacade.getAllRepairs();
        assertAll(
                () -> assertThat(allRepairs.get(0).date()).isEqualTo(LocalDate.of(2022,5,6)),
                () -> assertThat(allRepairs.get(1).date()).isEqualTo(LocalDate.of(2022,5,6)),
                () -> assertThat(allRepairs.get(2).date()).isEqualTo(LocalDate.of(2022,5,6)),
                () -> assertThat(allRepairs.get(3).date()).isEqualTo(LocalDate.of(2022,5,7)),
                () -> assertThat(allRepairs.get(4).date()).isEqualTo(LocalDate.of(2022,5,7)),
                () -> assertThat(allRepairs.get(5).date()).isEqualTo(LocalDate.of(2022,5,7)),
                () -> assertThat(allRepairs.get(6).date()).isEqualTo(LocalDate.of(2022,5,8)),
                () -> assertThat(allRepairs.get(7).date()).isEqualTo(LocalDate.of(2022,5,8)),
                () -> assertThat(allRepairs.get(8).date()).isEqualTo(LocalDate.of(2022,5,8)),
                () -> assertThat(allRepairs.get(9).date()).isEqualTo(LocalDate.of(2022,5,9))
        );
    }

    @Test
    void should_get_all_repairs_for_user() {
        //given
        Long searchedUserId = 1L;
        sampleRepairScenarios.saveFourRepairsInDatabase();
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

    private void reportRepairsManyTimes(int quantity) {
        for (int i = 0; i < quantity; i++) {
            repairFacade.reportRepair(CreateRepairRequest.builder()
                    .userId(1L)
                    .description("Description")
                    .build());
        }
    }

    private void saveThreePartsInDatabase() {
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
    }

    private static BigDecimal sumPartsPrices(List<RepairPart> repairParts) {
        return repairParts.stream()
                .map(repairPart -> repairPart.getPart().getPrice().multiply(BigDecimal.valueOf(repairPart.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}
