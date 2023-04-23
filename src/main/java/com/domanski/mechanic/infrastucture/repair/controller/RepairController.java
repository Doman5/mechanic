package com.domanski.mechanic.infrastucture.repair.controller;

import com.domanski.mechanic.domain.repair.RepairFacade;
import com.domanski.mechanic.domain.repair.dto.CreateRepairRequest;
import com.domanski.mechanic.domain.repair.dto.RepairReportResponse;
import com.domanski.mechanic.domain.repair.dto.RepairResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/repairs")
@AllArgsConstructor
public class RepairController {

    private final RepairFacade repairFacade;

    @GetMapping("/{id}")
    public ResponseEntity<RepairResponse> getRepair(@PathVariable Long id) {
        RepairResponse repair = repairFacade.getRepair(id);
        return ResponseEntity.ok(repair);
    }

    @PostMapping
    public ResponseEntity<RepairReportResponse> reportRepair(
            @RequestBody @Valid CreateRepairRequest createRepairRequest,
            @AuthenticationPrincipal String username) {
        RepairReportResponse repairReportResponse = repairFacade.reportRepair(createRepairRequest, username);
        return new ResponseEntity<>(repairReportResponse, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<List<RepairResponse>> getUserRepairs(@AuthenticationPrincipal String username) {
        List<RepairResponse> userRepairs = repairFacade.getUserRepairs(username);
        return ResponseEntity.ok(userRepairs);
    }
}
