package com.domanski.mechanic.domain.repair;

import com.domanski.mechanic.domain.repair.model.Repair;
import com.domanski.mechanic.domain.repair.model.RepairStatus;
import com.domanski.mechanic.domain.repair.repository.RepairRepository;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.Collections;

@AllArgsConstructor
public class SampleRepairScenarios {

    private final RepairRepository repairRepository;

    public  void saveOneRepairInDatabase() {
        Repair repair = Repair.builder()
                .userId(1L)
                .description("Simple description")
                .repairStatus(RepairStatus.AWAITING)
                .date(LocalDate.of(2022, 5, 5))
                .repairParts(Collections.emptyList())
                .build();
        repairRepository.save(repair);
    }

    public  void saveFourRepairsInDatabase() {
        Repair repair1 = Repair.builder()
                .userId(1L)
                .description("description 1")
                .repairStatus(RepairStatus.AWAITING)
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
                .repairStatus(RepairStatus.AWAITING)
                .repairParts(Collections.emptyList())
                .build();
        repairRepository.save(repair3);
        Repair repair4 = Repair.builder()
                .userId(1L)
                .description("description 4")
                .repairStatus(RepairStatus.AWAITING)
                .repairParts(Collections.emptyList())
                .build();
        repairRepository.save(repair4);
    }

    public void createNewRepair() {
        repairRepository.save(Repair.builder()
                .description("description 1")
                .userId(1L)
                .workTime(0.0)
                .repairParts(Collections.emptyList())
                .build());
    }
}
