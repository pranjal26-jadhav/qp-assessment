package com.grocery.service;

import com.grocery.dtos.InventoryDto;
import com.grocery.dtos.ListProductsDto;
import com.grocery.dtos.ProductDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.util.UriComponentsBuilder;

public interface ProductService {
    ProductDto addProduct(ProductDto createProductDto);

    ListProductsDto getAllProducts(Pageable pageable, UriComponentsBuilder uriBuilder);

    void deleteProduct(Long productId);

    ProductDto updateProduct(Long productId, ProductDto productDto);

    ProductDto updateProductInventory(Long productId, InventoryDto inventoryDto);

    ListProductsDto getAvailableProducts(Pageable pageable, UriComponentsBuilder uriBuilder);
}
