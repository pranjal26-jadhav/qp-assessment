package com.grocery.service;

import com.grocery.dtos.OrderDto;

public interface OrderService {

    OrderDto saveOrder(OrderDto orderDto, String userName);
}
