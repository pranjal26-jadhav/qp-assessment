package com.grocery.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderLineDto {
    private Long productId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String productName;
    int quantity;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal amount;

}

// Need to understand how product price should be used.
