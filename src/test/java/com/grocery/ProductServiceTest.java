package com.grocery;

import com.grocery.dtos.MeasurementUnitDto;
import com.grocery.dtos.ProductDto;
import com.grocery.service.impl.ProductServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;

@Slf4j
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackages = "com.grocery.exceptionHandler")
public class ProductServiceTest {

    @Autowired
    private ProductServiceImpl productService;


    @Test
    public void addProductTest() {
        ProductDto productDto = new ProductDto();
        productDto.setName("");
        productDto.setDescription("oil");
        productDto.setPrice(BigDecimal.valueOf(100));
        productDto.setInventory(50);
        MeasurementUnitDto unitDto = new MeasurementUnitDto();
        unitDto.setId(1L);
        productDto.setMeasurementUnit(unitDto);
        ProductDto productDto1 = productService.addProduct(productDto);

    }
}
