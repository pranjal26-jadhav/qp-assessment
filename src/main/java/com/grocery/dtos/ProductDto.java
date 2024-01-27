package com.grocery.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grocery.entitites.ProductEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String name;
    private String description;
    private int inventory;
    private BigDecimal price;
    private MeasurementUnitDto measurementUnit;


    public ProductDto convertEntityToProductDto(ProductEntity productEntity) {
        this.id = productEntity.getId();
        this.name = productEntity.getName();
        this.description = productEntity.getDescription();
        this.inventory =  productEntity.getAvailableInventory();
        this.price = productEntity.getPrice();
        MeasurementUnitDto measurementUnitDto = new MeasurementUnitDto();
        measurementUnitDto.setId(productEntity.getMeasurementUnit().getId());
        measurementUnitDto.setUnit(productEntity.getMeasurementUnit().getUnit());
        this.measurementUnit = measurementUnitDto;
        return this;
    }

}
