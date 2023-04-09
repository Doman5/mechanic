package com.domanski.mechanic.domain.repair.utils;

import com.domanski.mechanic.domain.repair.dto.PartRequest;
import com.domanski.mechanic.domain.repair.dto.PartsAndWorkTimeRequest;
import com.domanski.mechanic.domain.repair.error.PartNoFoundException;
import com.domanski.mechanic.domain.repair.error.RepairPartNoFoundException;
import com.domanski.mechanic.domain.repair.model.Part;
import com.domanski.mechanic.domain.repair.model.Repair;
import com.domanski.mechanic.domain.repair.model.RepairPart;
import com.domanski.mechanic.domain.repair.repository.PartRepository;
import com.domanski.mechanic.domain.repair.repository.RepairPartRepository;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class RepairUsedPartManager {

    private final RepairPartRepository repairPartRepository;
    private final PartRepository partRepository;

    public void addPartsToRepair(Repair repair, PartsAndWorkTimeRequest partsAndWorkTimeRequest) {
        List<PartRequest> partsFromRequest = partsAndWorkTimeRequest.parts();
        changeQuantityToPartsWhichWereUsedBefore(partsFromRequest);
        addPartsWhichWereNotUserBefore(repair, partsFromRequest);
    }

    private void addPartsWhichWereNotUserBefore(Repair repair, List<PartRequest> partsFromRequest) {
        partsFromRequest.stream()
                .filter(partRequest -> !repairPartRepository.existsById(partRequest.partId()))
                .forEach(partRequest -> {
                    Part part = partRepository.findById(partRequest.partId())
                            .orElseThrow(() -> new PartNoFoundException("Part with id %d no found".formatted(partRequest.partId())));
                    RepairPart savedRepairPart = repairPartRepository.save(RepairPart.builder()
                            .part(part)
                            .repair(repair)
                            .quantity(partRequest.quantity())
                            .build());
                    List<RepairPart> newRepairParts = new ArrayList<>(repair.getRepairParts());
                    newRepairParts.add(savedRepairPart);
                    repair.setRepairParts(newRepairParts);
                });
    }

    private void changeQuantityToPartsWhichWereUsedBefore(List<PartRequest> partsFromRequest) {
        partsFromRequest.stream()
                .filter(partRequest -> repairPartRepository.existsById(partRequest.partId()))
                .forEach(partRequest -> {
                    RepairPart repairPart = repairPartRepository.findById(partRequest.partId())
                            .orElseThrow(() -> new RepairPartNoFoundException("Repair part no found"));
                    repairPart.setQuantity(repairPart.getQuantity() + partRequest.quantity());
                    repairPartRepository.save(repairPart);
                });
    }
}
