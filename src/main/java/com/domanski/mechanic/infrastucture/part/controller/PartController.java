package com.domanski.mechanic.infrastucture.part.controller;

import com.domanski.mechanic.domain.part.PartFacade;
import com.domanski.mechanic.domain.part.dto.PartRequest;
import com.domanski.mechanic.domain.part.dto.PartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/parts")
public class PartController {

    private final PartFacade partFacade;

    @GetMapping
    public ResponseEntity<List<PartResponse>> getAllParts() {
        List<PartResponse> parts = partFacade.getParts();
        return ResponseEntity.ok(parts);
    }

    @PostMapping
    public ResponseEntity<PartResponse> addPart(@RequestBody @Valid PartRequest partRequest) {
        PartResponse part = partFacade.addPart(partRequest);
        return new ResponseEntity<>(part, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartResponse> updatePart(@PathVariable Long id, @RequestBody PartRequest partRequest) {
        PartResponse part = partFacade.updatePart(id, partRequest);
        return ResponseEntity.ok(part);
    }

}
