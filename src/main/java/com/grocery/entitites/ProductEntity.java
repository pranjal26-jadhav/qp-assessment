package com.grocery.entitites;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;


@Getter
@Setter
@Table(name = "product")
@Entity
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(name = "measurement_unit", referencedColumnName = "id", nullable = false)
    private MeasurementUnitEntity measurementUnit;

    @Column(name = "price", nullable = false)
    private BigDecimal price; //

    @Column(name = "available_inventory")
    private int availableInventory;

    @Column(name = "isDeleted")
    private Boolean isDeleted;

}
