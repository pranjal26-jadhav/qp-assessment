package com.grocery.service.impl;

import com.grocery.dtos.*;
import com.grocery.entitites.MeasurementUnitEntity;
import com.grocery.entitites.ProductEntity;
import com.grocery.exceptionHandler.HttpException;
import com.grocery.repos.MeasurementUnitRepository;
import com.grocery.repos.ProductRepository;
import com.grocery.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    public static final String PAGE = "page";
    public static final String PAGE_SIZE = "pageSize";
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MeasurementUnitRepository unitRepository;

    @Override
    @Transactional
    public ProductDto addProduct(ProductDto createProductDto) {

        try {

            ProductEntity productEntity = new ProductEntity();
            productEntity.setIsDeleted(false);

            productEntity.setName(Optional.ofNullable(createProductDto)
                    .map(ProductDto::getName)
                    .filter(StringUtils::isNotBlank)
                    .orElseThrow(() -> new HttpException(HttpStatus.BAD_REQUEST,"Product name can not be empty")));

            Optional.ofNullable(createProductDto)
                    .map(ProductDto::getDescription)
                    .ifPresent(productEntity::setDescription);

            Integer inventory = Optional.ofNullable(createProductDto)
                    .map(ProductDto::getInventory)
                    .filter(q -> q > 0)
                    .orElse(0);

            productEntity.setAvailableInventory(inventory);

            productEntity.setPrice(Optional.ofNullable(createProductDto)
                     .map(ProductDto::getPrice)
                     .filter(price -> price.signum() > 0)
                     .orElseThrow(() -> new HttpException(HttpStatus.BAD_REQUEST, "Price should be greater than zero")));

            productEntity.setMeasurementUnit(Optional.ofNullable(createProductDto)
                    .map(ProductDto::getMeasurementUnit)
                    .map(MeasurementUnitDto::getId)
                    .flatMap(id -> unitRepository.findById(id))
                    .orElseThrow(() -> new HttpException(HttpStatus.BAD_REQUEST,"measurement Unit selected is invalid")));
            ProductEntity savedProductEntity = saveProduct(productEntity);
            ProductDto product = new ProductDto();
            return product.convertEntityToProductDto(savedProductEntity);
        } catch (Exception e) {
            log.error("Error in adding product: {}", createProductDto.getName(), e);
            throw e;
        }

    }

    @Override
    public ListProductsDto getAllProducts(Pageable pageable, UriComponentsBuilder uriBuilder) {
        try {
            Page<ProductEntity> page = productRepository.findAll(pageable);
            log.debug("total products are :{}", page.getTotalElements());
            List<ProductDto> productDtos = page.stream().map(productEntity -> {
                ProductDto productDto = new ProductDto();
                return productDto.convertEntityToProductDto(productEntity);
            }).collect(Collectors.toList());
            String next = null;
            ListProductsDto listProductsDto = new ListProductsDto();
            Paging.PagingBuilder pagingBuilder = Paging.builder().totalPages(page.getTotalPages()).hasNext(page.hasNext());
            uriBuilder.path("/v1/products");
            listProductsDto.setProducts(productDtos);
            if (page.hasNext()) {
                next = uriBuilder.replaceQueryParam(PAGE, pageable.getPageNumber() + 1)
                        .replaceQueryParam(PAGE_SIZE, pageable.getPageSize())
                        .toUriString();
            } else {
                next = null;
            }
            pagingBuilder.next(Paging.Next.builder().link(next).build());
            listProductsDto.setPaging(pagingBuilder.build());
            return listProductsDto;
        } catch (Exception e) {
            log.error("Error in getting all products: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void deleteProduct(Long productId) {
        try {
            Optional<ProductEntity> productEntity = findProductById(productId);
            productEntity.map(product -> {
                product.setIsDeleted(true);
                return productRepository.save(product);
            }).orElseThrow(() -> new HttpException(HttpStatus.BAD_REQUEST, "Product does not exist"));
        } catch (Exception e) {
            log.error("Error in updating delete status for product: {}", productId, e);
            throw e;
        }
    }

    @Override
    public ProductDto updateProduct(Long productId, ProductDto productDto) {
        try {
        ProductEntity productEntity = findProductById(productId)
                .orElseThrow(() -> new HttpException(HttpStatus.BAD_REQUEST, "Product does not exist with id: " + productId));

        Optional.ofNullable(productDto)
                .map(ProductDto::getDescription)
                .ifPresent(productEntity::setDescription);

        Optional.ofNullable(productDto)
                .map(ProductDto::getName)
                .filter(StringUtils::isNotBlank)
                .ifPresent(productEntity::setName);

        Optional.ofNullable(productDto)
                .map(ProductDto::getPrice)
                .ifPresent(productEntity::setPrice);

        Optional.ofNullable(productDto)
                .map(ProductDto::getMeasurementUnit)
                .map(MeasurementUnitDto::getId)
                .ifPresent(unitId -> {
                    productEntity.setMeasurementUnit(findMeasurementUnitById(unitId)
                            .orElseThrow(() -> new HttpException(HttpStatus.BAD_REQUEST,"Measurement unit id not found")));
                });
            ProductEntity updatedProductEntity = saveProduct(productEntity);
            log.info("Product with id :{} updated successfully", productId);
            return productDto.convertEntityToProductDto(updatedProductEntity);
        } catch (Exception e) {
            log.error("Error in adding product: {}", productDto.getName(), e);
            throw e;
        }

    }
    @Override
    public ProductDto updateProductInventory(Long productId, InventoryDto inventoryDto) {
        try {
            ProductEntity productEntity = findProductById(productId)
                    .orElseThrow(() -> new HttpException(HttpStatus.BAD_REQUEST, "Could not find product with id: " + productId));
            int result = productEntity.getAvailableInventory() + inventoryDto.getQuantity();
            int max = Math.max(result, 0);
            log.info("Updating product: '{}' inventory to '{}'",productId, max);
            productEntity.setAvailableInventory(max);
            ProductEntity savedProductEntity = saveProduct(productEntity);
            ProductDto productDto = new ProductDto();
            return productDto.convertEntityToProductDto(savedProductEntity);
        } catch (Exception e) {
            log.error("Error in updating inventory for product: {}", productId, e);
            throw e;
        }
    }

    @Override
    public ListProductsDto getAvailableProducts(Pageable pageable, UriComponentsBuilder uriBuilder) {
        try {
            Page<ProductEntity> page = productRepository.findByAvailableInventoryGreaterThanAndIsDeletedFalse(0, pageable);
            log.info("total products are: {}", page.getTotalElements());
            List<ProductDto> productDtoList = page.stream().map(productEntity -> {
                ProductDto productDto = new ProductDto();
                return productDto.convertEntityToProductDto(productEntity);
            }).collect(Collectors.toList());
            String next = null;
            ListProductsDto listProductsDto = new ListProductsDto();
            Paging.PagingBuilder pagingBuilder = Paging.builder().totalPages(page.getTotalPages()).hasNext(page.hasNext());
            uriBuilder.path("/v1/products/available");
            listProductsDto.setProducts(productDtoList);
            if (page.hasNext()) {
                next = uriBuilder.replaceQueryParam(PAGE, pageable.getPageNumber() + 1)
                        .replaceQueryParam(PAGE_SIZE, pageable.getPageSize())
                        .toUriString();
            } else {
                next = null;
            }
            pagingBuilder.next(Paging.Next.builder().link(next).build());
            listProductsDto.setPaging(pagingBuilder.build());
            return listProductsDto;
        } catch (Exception e) {
            log.error("Error in getting available products for page: {}", pageable.getPageNumber(), e);
            throw e;
        }
    }

    public Optional<ProductEntity> findProductById(Long productId) {
        return productRepository.findById(productId);
    }

    public Optional<MeasurementUnitEntity> findMeasurementUnitById(Long id) {
        log.info("Get measurement unit for id :{}", id);
        return unitRepository.findById(id);
    }

    public ProductEntity saveProduct(ProductEntity productEntity) {
        return productRepository.save(productEntity);
    }

}
