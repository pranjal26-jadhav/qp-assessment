package com.grocery.repos;

import com.grocery.entitites.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Page<ProductEntity> findByAvailableInventoryGreaterThanAndIsDeletedFalse(int availableInventory, Pageable pageable);
}
