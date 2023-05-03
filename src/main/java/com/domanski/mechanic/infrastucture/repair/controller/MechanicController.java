package com.domanski.mechanic.infrastucture.repair.controller;

import com.domanski.mechanic.domain.repair.RepairFacade;
import com.domanski.mechanic.domain.repair.dto.PartsAndWorkTimeRequest;
import com.domanski.mechanic.domain.repair.dto.RepairResponse;
import com.domanski.mechanic.domain.repair.model.RepairStatus;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mechanic/repairs")
@AllArgsConstructor
public class MechanicController {

    private final RepairFacade repairFacade;

    @GetMapping
    public ResponseEntity<List<RepairResponse>> getAllRepairs(@RequestParam(defaultValue = "AWAITING") RepairStatus repairStatus) {
        List<RepairResponse> allRepairs = repairFacade.getAllRepairsByStatus(repairStatus);
        return ResponseEntity.ok(allRepairs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepairResponse> getRepair(@PathVariable Long id) {
        RepairResponse repair = repairFacade.getRepair(id);
        return ResponseEntity.ok(repair);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepairResponse> doRepair(@PathVariable Long id, @RequestBody @Valid PartsAndWorkTimeRequest partsAndWorkTimeRequest) {
        RepairResponse repairResponse = repairFacade.doRepairWithPartsAndWorkTime(id, partsAndWorkTimeRequest);
        return ResponseEntity.ok(repairResponse);
    }

    @GetMapping("/{id}/finish")
    public ResponseEntity<RepairResponse> finishRepair(@PathVariable Long id) {
        RepairResponse repairResponse = repairFacade.finishRepair(id);
        return ResponseEntity.ok(repairResponse);
    }
}
