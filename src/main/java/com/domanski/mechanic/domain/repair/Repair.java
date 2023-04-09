package com.domanski.mechanic.domain.repair;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "repairs")
public class Repair {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String description;
        private LocalDate date;
        @OneToMany
        @JoinColumn(name = "repair_id")
        private List<RepairPart> repairParts;
        private Double workTime;
        private BigDecimal repairCost;
        @Enumerated(value = EnumType.STRING)
        private RepairStatus repairStatus;
        private Long userId;
}