package com.grocery.handlers;

import com.grocery.dtos.OrderDto;
import com.grocery.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/v1")
public class OrderController {

    @Autowired
    private OrderService orderService;

    //Take user id from principal
    @PostMapping(value = "/order")
    @PreAuthorize(value = "hasRole('USER')")
    public ResponseEntity<OrderDto> saveOrder(@RequestBody OrderDto orderDto, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        log.info("Saving order for user: {}", username);
        OrderDto order = orderService.saveOrder(orderDto, username);
        return ResponseEntity.ok(order);
    }
}
