package com.domanski.mechanic.domain.repair.repository;

import com.domanski.mechanic.domain.repair.Repair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RepairRepository extends JpaRepository<Repair, Long> {
    List<Repair> findAllByUserId(Long userId);
    List<Repair> findAllByDate(LocalDate date);
}
