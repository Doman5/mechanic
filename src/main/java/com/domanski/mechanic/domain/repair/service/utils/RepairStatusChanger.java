package com.domanski.mechanic.domain.repair.service.utils;

import com.domanski.mechanic.domain.repair.error.RepairStatusException;
import com.domanski.mechanic.domain.repair.model.Repair;
import com.domanski.mechanic.domain.repair.model.RepairStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RepairStatusChanger {

    public static void changeRepairStatusToWorkInProgressIfWasNotChangedBefore(Repair repair) {
        if (repair.getRepairStatus() != RepairStatus.WORK_IN_PROGRESS) {
            repair.setRepairStatus(RepairStatus.WORK_IN_PROGRESS);
            log.info(String.format("Status for repair with id %d was changed", repair.getId()));
        }
    }

    public static void changeRepairStatusToFinished(Repair repair) {
        if (repair.getRepairStatus() == RepairStatus.FINISHED) {
            throw new RepairStatusException("Repair with %d was already finished".formatted(repair.getId()));
        }

        if (repair.getRepairStatus() != RepairStatus.WORK_IN_PROGRESS) {
            throw new RepairStatusException("Repair with id %d and status %s can not be finished, first start work on it".formatted(repair.getId(), repair.getRepairStatus()));
        }

        repair.setRepairStatus(RepairStatus.FINISHED);
        log.info("Repair with id %d has been finished".formatted(repair.getId()));

    }


}
