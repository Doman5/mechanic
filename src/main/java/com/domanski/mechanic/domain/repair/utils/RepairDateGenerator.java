package com.domanski.mechanic.domain.repair.utils;

import com.domanski.mechanic.domain.repair.model.Repair;
import com.domanski.mechanic.domain.repair.model.RepairStatus;
import com.domanski.mechanic.domain.repair.repository.RepairRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@AllArgsConstructor
@Slf4j
public class RepairDateGenerator {

    private final RepairRepository repairRepository;
    private final Long maximumRepairPerDay;
    private LocalDate date;

    public void generateDateAndUpdateRepairInformation(Repair repair) {
        date = findAvailableDate(date);
        repair.setDate(date);
        repair.setRepairStatus(RepairStatus.AWAITING);
        repairRepository.save(repair);
        log.info("Date %s was set for repair with id %d".formatted(date, repair.getId()));
    }

    private LocalDate findAvailableDate(LocalDate date) {
        long quantityOfRepairs = repairRepository.findAllByDate(date).size();
        if(quantityOfRepairs <= maximumRepairPerDay) {
            return date;
        }
        return findAvailableDate(date.plusDays(1));
    }
}
