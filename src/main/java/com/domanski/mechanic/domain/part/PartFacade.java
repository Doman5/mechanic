package com.domanski.mechanic.domain.part;

import com.domanski.mechanic.domain.common.Part;
import com.domanski.mechanic.domain.common.PartRepository;
import com.domanski.mechanic.domain.part.dto.PartRequest;
import com.domanski.mechanic.domain.part.dto.PartResponse;
import com.domanski.mechanic.domain.repair.error.PartNoFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class PartFacade {

    private final PartRepository partRepository;

    public List<PartResponse> getParts() {
        return partRepository.findAll().stream()
                .map(PartMapper::mapFromPart)
                .toList();
    }

    public PartResponse getPart(Long id) {
        return partRepository.findById(id)
                .map(PartMapper::mapFromPart)
                .orElseThrow(() -> new PartNoFoundException("Part with id %d no found".formatted(id)));
    }

    public PartResponse addPart(PartRequest partRequest) {
        Part partToSave = PartMapper.mapFromPartRequest(partRequest);
        Part savedPart = partRepository.save(partToSave);
        return PartMapper.mapFromPart(savedPart);

    }

    public PartResponse updatePart(Long id, PartRequest partRequest) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new PartNoFoundException("Part with id %d no found".formatted(id)));

        checkPartNameAndChangeItWhenIsDifferent(part, partRequest.name());
        checkPartPriceAndChangeItWhenIsDifferent(part, partRequest.price());
        Part updatedPart = partRepository.save(part);
        return PartMapper.mapFromPart(updatedPart);
    }

    public void deletePart(Long id) {
        partRepository.deleteById(id);
        log.info("Part with id %d was deleted".formatted(id));
    }


    private static void checkPartNameAndChangeItWhenIsDifferent(Part part, String newPartName) {
        if(!part.getName().equals(newPartName)) {
            part.setName(newPartName);
        }
    }

    private static void checkPartPriceAndChangeItWhenIsDifferent(Part part, BigDecimal newPartPrice) {
        if(!part.getPrice().equals(newPartPrice)) {
            part.setPrice(newPartPrice);
        }
    }
}
