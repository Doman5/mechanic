package com.domanski.mechanic.domain.repair;

import com.domanski.mechanic.domain.repair.dto.RepairDto;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
public class RepairFacade {

    public RepairDto getRepair(Long id) {
        return null;
    }

    public RepairDto addRepair(Repair repair) {
        return null;
    }

    public RepairDto updateRepair(Repair repair) {
        return null;
    }

    public void deleteRepair(Long id) {
    }

    public List<RepairDto> getUserRepairs(Long userId) {
        return null;
    }

    public List<RepairDto> getAllRepairsAfter(LocalDate date) {
        return null;
    }
}
