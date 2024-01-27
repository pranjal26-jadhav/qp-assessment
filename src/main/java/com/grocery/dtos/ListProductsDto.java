package com.grocery.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListProductsDto {
    List<ProductDto> products;
    Paging paging;
}