package com.domanski.mechanic.domain.repair;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "repairs")
public class Repair {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id;
        String description;
        LocalDate date;
        @OneToMany(mappedBy = "repairId")
        List<Part> parts;
        Double workTime;
        BigDecimal repairCost;
        @Enumerated(value = EnumType.STRING)
        RepairStatus repairStatus;
}