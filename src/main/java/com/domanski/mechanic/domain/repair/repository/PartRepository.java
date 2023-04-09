package com.domanski.mechanic.domain.repair.repository;

import com.domanski.mechanic.domain.repair.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {
}
