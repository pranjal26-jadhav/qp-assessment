package com.grocery.dtos;

import com.grocery.entitites.OrderEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class OrderDto {
    private String orderId;
    private List<OrderLineDto> orders;
    private String userName;
    private BigDecimal orderAmount;

    public OrderDto convertEntityToDto(OrderEntity orderEntity) {
        this.orderId = String.valueOf(orderEntity.getId());
        List<OrderLineDto> orderLines = orderEntity.getOrderLineEntities().stream()
                .map(orderLineEntity -> {
                    OrderLineDto orderLine = new OrderLineDto();
                    orderLine.setProductId(orderLineEntity.getId());
                    orderLine.setProductName(orderLineEntity.getProductEntity().getName());
                    orderLine.setQuantity(orderLineEntity.getQuantity());
                    return orderLine;
                }).collect(Collectors.toList());
        this.orders = orderLines;
        this.orderAmount = orderEntity.getTotal_amount();
        return this;
    }
}
