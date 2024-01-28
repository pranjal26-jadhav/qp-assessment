package com.grocery.service.impl;

import com.grocery.dtos.OrderDto;
import com.grocery.dtos.OrderLineDto;
import com.grocery.entitites.OrderEntity;
import com.grocery.entitites.OrderLineEntity;
import com.grocery.entitites.ProductEntity;
import com.grocery.entitites.UserEntity;
import com.grocery.enums.OrderStatus;
import com.grocery.exceptionHandler.HttpException;
import com.grocery.repos.OrderLineRepository;
import com.grocery.repos.OrderRepository;
import com.grocery.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderLineRepository orderLineRepository;
    @Override
    @Transactional
    public OrderDto saveOrder(OrderDto orderDto, String userName) {
        try {
            AtomicReference<BigDecimal> total = new AtomicReference<>(new BigDecimal(0));
            UserEntity userEntity = Optional.ofNullable(userName)
                    .filter(StringUtils::isNotBlank)
                    .flatMap(user -> userService.findUserByUsername(user))
                    .orElseThrow(() -> new HttpException(HttpStatus.INTERNAL_SERVER_ERROR ,"User does not exist"));
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setUserEntity(userEntity);

            Set<OrderLineEntity> orderLineEntities = orderDto.getOrders()
                    .stream()
                    .map(orderLine -> {
                        Long productId = orderLine.getProductId();
                        ProductEntity productEntity = productService.findProductById(productId) //TODO: quantity check on query
                                .orElseThrow(() -> new RuntimeException("Product with id does exist: " + productId));
                        int availableInventory = productEntity.getAvailableInventory();

                        if (orderLine.getQuantity() <= 0) {
                            throw new HttpException(HttpStatus.BAD_REQUEST,"Please select Quantity greater than 0");
                        }

                        if (availableInventory >= orderLine.getQuantity()) {
                            OrderLineEntity orderLineEntity = getOrderLineEntity(orderLine, productEntity);
                            orderLineEntity.setOrder(orderEntity);
                            total.set(total.get().add(orderLineEntity.getOrderLinePrice()));
                            productEntity.setAvailableInventory(availableInventory - orderLine.getQuantity());
                            productService.saveProduct(productEntity); //TODO: Need to do Optimistic locking for dirty data. Separate out inventory from ProductEntity
                            return orderLineEntity;
                        } else {
                            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR,"Product: " + productEntity.getName() + " is out of stock");
                        }
                    }).collect(Collectors.toSet());

            orderEntity.setOrderLineEntities(orderLineEntities);
            orderEntity.setTotal_amount(total.get());
            orderEntity.setOrderStatus(OrderStatus.Placed);
            OrderEntity savedOrderEntity = orderRepository.save(orderEntity);
            OrderDto order = new OrderDto();
            order.convertEntityToDto(savedOrderEntity);
            return order;
        } catch (Exception e) {
            log.error("Error in saving order: ", e);
            throw e;
        }
    }

    private static OrderLineEntity getOrderLineEntity(OrderLineDto orderLine, ProductEntity productEntity) {
        OrderLineEntity orderLineEntity = new OrderLineEntity();
        orderLineEntity.setProductEntity(productEntity);
        orderLineEntity.setQuantity(orderLine.getQuantity());
        BigDecimal orderLineSum = productEntity.getPrice().multiply(BigDecimal.valueOf(orderLine.getQuantity()));
        orderLineEntity.setOrderLinePrice(orderLineSum);
        return orderLineEntity;
    }
}
