package com.domanski.mechanic.domain.repair.utils;

import com.domanski.mechanic.domain.common.Part;
import com.domanski.mechanic.domain.common.PartRepository;
import com.domanski.mechanic.domain.repair.dto.PartsAndWorkTimeRequest;
import com.domanski.mechanic.domain.repair.dto.UsedPartRequest;
import com.domanski.mechanic.domain.repair.error.PartNoFoundException;
import com.domanski.mechanic.domain.repair.error.RepairPartNoFoundException;
import com.domanski.mechanic.domain.repair.model.Repair;
import com.domanski.mechanic.domain.repair.model.RepairPart;
import com.domanski.mechanic.domain.repair.repository.RepairPartRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class RepairUsedPartManager {

    private final RepairPartRepository repairPartRepository;
    private final PartRepository partRepository;

    public void addPartsToRepair(Repair repair, PartsAndWorkTimeRequest partsAndWorkTimeRequest) {
        List<UsedPartRequest> partsFromRequest = partsAndWorkTimeRequest.parts();
        changeQuantityToPartsWhichWereUsedBefore(partsFromRequest);
        addPartsWhichWereNotUserBefore(repair, partsFromRequest);
    }

    private void addPartsWhichWereNotUserBefore(Repair repair, List<UsedPartRequest> partsFromRequest) {
        partsFromRequest.stream()
                .filter(partRequest -> !repairPartRepository.existsById(partRequest.partId()))
                .forEach(partRequest -> {
                    Part part = partRepository.findById(partRequest.partId())
                            .orElseThrow(() -> new PartNoFoundException("Part with id %d no found".formatted(partRequest.partId())));
                    repairPartRepository.save(RepairPart.builder()
                            .part(part)
                            .repair(repair)
                            .quantity(partRequest.quantity())
                            .build());
                });
    }

    private void changeQuantityToPartsWhichWereUsedBefore(List<UsedPartRequest> partsFromRequest) {
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
