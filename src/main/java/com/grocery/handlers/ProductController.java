package com.grocery.handlers;

import com.grocery.dtos.InventoryDto;
import com.grocery.dtos.ListProductsDto;
import com.grocery.dtos.ProductDto;
import com.grocery.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
@RequestMapping(value = "/v1")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping(value = "/product")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto createProductDto) {
        log.info("Adding product: '{}'", createProductDto.getName());
        //TODO: Incomplete
        ProductDto product = productService.addProduct(createProductDto);
        return ResponseEntity.ok(product);
    }

    @GetMapping(value = "/products")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<ListProductsDto> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int pageSize, UriComponentsBuilder uriBuilder)  {
        log.info("Getting products for page number: '{}' pageSize: '{}'", page, pageSize);
        Pageable pageable = PageRequest.of(page, pageSize);
        ListProductsDto products = productService.getAllProducts(pageable, uriBuilder);
        return ResponseEntity.ok(products);
    }

    @DeleteMapping(value = "/product/{productId}")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long productId) {
        log.info("Delete product with Id: '{}'", productId);
        productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping(value = "/product/{productId}")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto, @PathVariable("productId") Long productId) {
        log.info("Updating product with id: '{}'", productId);
         return ResponseEntity.ok(productService.updateProduct(productId, productDto));
    }

    @PatchMapping(value = "/product/{productId}/inventory")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<ProductDto> updateProductInventory(@RequestBody InventoryDto inventoryDto, @PathVariable("productId") Long productId) {
        log.info("Updating inventory for product with id: '{}' quantity: '{}'", productId, inventoryDto.getQuantity());
        ProductDto productDto = productService.updateProductInventory(productId, inventoryDto);
        return ResponseEntity.ok(productDto);
    }

    @GetMapping(value = "/products/available")
    @PreAuthorize(value = "hasRole('USER')")
    public ResponseEntity<ListProductsDto> getAvailableProducts(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int pageSize, UriComponentsBuilder uriBuilder) {
        log.info("Get available products from page: '{}' pageSize: '{}'", page, pageSize);
        Pageable pageable = PageRequest.of(page, pageSize);
        ListProductsDto availableProducts = productService.getAvailableProducts(pageable, uriBuilder);
        return ResponseEntity.ok(availableProducts);
    }
}
