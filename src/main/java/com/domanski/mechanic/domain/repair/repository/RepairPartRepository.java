package com.domanski.mechanic.domain.repair.repository;

import com.domanski.mechanic.domain.repair.model.RepairPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairPartRepository extends JpaRepository<RepairPart, Long> {
}
