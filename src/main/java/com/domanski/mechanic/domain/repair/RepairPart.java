package com.domanski.mechanic.domain.repair;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "repairs_parts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepairPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "repair_id")
    private Repair repair;
    @ManyToOne
    @JoinColumn(name = "part_id")
    private Part part;
    private Long quantity;
}
