package com.domanski.mechanic.domain.repair.service;

import com.domanski.mechanic.domain.repair.dto.RepairResponse;
import com.domanski.mechanic.domain.repair.error.RepairNoFoundException;
import com.domanski.mechanic.domain.repair.model.RepairStatus;
import com.domanski.mechanic.domain.repair.repository.RepairRepository;
import com.domanski.mechanic.domain.repair.service.utils.RepairMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class RepairService {

    private final RepairRepository repairRepository;

    public RepairResponse getRepair(Long id) {
        return repairRepository.findById(id)
                .map(RepairMapper::mapFromRepair)
                .orElseThrow(() -> new RepairNoFoundException("Repair with id %d no found".formatted(id)));
    }

    public List<RepairResponse> getAllRepairsByStatus(RepairStatus repairStatus) {
        return repairRepository.findAllByRepairStatus(repairStatus).stream()
                .map(RepairMapper::mapFromRepair)
                .toList();
    }
}
