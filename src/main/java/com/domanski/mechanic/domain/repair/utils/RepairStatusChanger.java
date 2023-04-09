package com.domanski.mechanic.domain.repair.utils;

import com.domanski.mechanic.domain.repair.model.Repair;
import com.domanski.mechanic.domain.repair.model.RepairStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RepairStatusChanger {

    public static void changeRepairStatusToWorkInProgressIfWasNotChangedBefore(Repair repair) {
        if(repair.getRepairStatus() != RepairStatus.WORK_IN_PROGRESS) {
            repair.setRepairStatus(RepairStatus.WORK_IN_PROGRESS);
            log.info(String.format("Status for repair with id %d was changed", repair.getId()));
        }
    }
}
