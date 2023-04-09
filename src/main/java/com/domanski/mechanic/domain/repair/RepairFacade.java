package com.domanski.mechanic.domain.repair;

import com.domanski.mechanic.domain.repair.dto.CreateRepairRequest;
import com.domanski.mechanic.domain.repair.dto.PartRequest;
import com.domanski.mechanic.domain.repair.dto.PartsAndWorkTimeRequest;
import com.domanski.mechanic.domain.repair.dto.RepairDto;
import com.domanski.mechanic.domain.repair.error.PartNoFoundException;
import com.domanski.mechanic.domain.repair.error.RepairNoFoundException;
import com.domanski.mechanic.domain.repair.error.RepairPartNoFoundException;
import com.domanski.mechanic.domain.repair.repository.PartRepository;
import com.domanski.mechanic.domain.repair.repository.RepairPartRepository;
import com.domanski.mechanic.domain.repair.repository.RepairRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class RepairFacade {

    private final RepairRepository repairRepository;
    private final PartRepository partRepository;
    private final RepairPartRepository repairPartRepository;

    @Value("${app.repair.priceForOneWorkHour}")
    private final double priceForOneWorkHour;

    public RepairDto getRepair(Long id) {
        return repairRepository.findById(id)
                .map(RepairMapper::mapFromRepair)
                .orElseThrow(() -> new RepairNoFoundException("Repair with partId %d no found".formatted(id)));
    }

    public RepairDto createRepair(CreateRepairRequest createRepairRequest) {
        Repair repairToSave = Repair.builder()
                .description(createRepairRequest.description())
                .userId(createRepairRequest.userId())
                .repairStatus(RepairStatus.DATE_NOT_SPECIFIED)
                .workTime(0.0)
                .repairParts(Collections.emptyList())
                .repairCost(BigDecimal.ZERO)
                .build();
        Repair savedRepair = repairRepository.save(repairToSave);
        return RepairMapper.mapFromRepair(savedRepair);
    }

    public RepairDto doRepairWithPartsAndWorkTime(Long id, PartsAndWorkTimeRequest partsAndWorkTimeRequest) {
        Repair repair = repairRepository.findById(id)
                .orElseThrow(() -> new RepairNoFoundException("Repair with %d no found".formatted(id)));

        List<PartRequest> partsFromRequest = partsAndWorkTimeRequest.parts();

        for (PartRequest partRequest : partsFromRequest) {
            if (repairPartRepository.existsById(partRequest.partId())) {
                RepairPart repairPart = repairPartRepository.findById(partRequest.partId())
                        .orElseThrow(() -> new RepairPartNoFoundException("Repair part no found"));
                repairPart.setQuantity(repairPart.getQuantity() + partRequest.quantity());
                repairPartRepository.save(repairPart);
            } else {
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
            }
        }

        repair.setWorkTime(repair.getWorkTime() + partsAndWorkTimeRequest.workiTime());

        BigDecimal priceForWorkTime = BigDecimal.valueOf(repair.getWorkTime() * priceForOneWorkHour);
        BigDecimal priceForParts = repair.getRepairParts().stream()
                .map(repairPart -> repairPart.getPart().getPrice().multiply(BigDecimal.valueOf(repairPart.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        repair.setRepairCost(priceForParts.add(priceForWorkTime));

        if(repair.getRepairStatus() != RepairStatus.WORK_IN_PROGRESS) {
            repair.setRepairStatus(RepairStatus.WORK_IN_PROGRESS);
        }

        Repair savedRepair = repairRepository.save(repair);
        return RepairMapper.mapFromRepair(savedRepair);
    }

    public List<RepairDto> getUserRepairs(Long userId) {
        return repairRepository.findAllByUserId(userId).stream()
                .map(RepairMapper::mapFromRepair)
                .toList();
    }

    public long checkRepairQuantityForDate(LocalDate date) {
        return repairRepository.findAllByDate(date).size();
    }

}
