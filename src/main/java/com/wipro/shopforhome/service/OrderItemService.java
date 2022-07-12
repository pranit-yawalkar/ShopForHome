package com.wipro.shopforhome.service;

import com.wipro.shopforhome.model.OrderItem;
import com.wipro.shopforhome.repository.OrderItemRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    public OrderItem addOrderedProducts(OrderItem orderItem) {
        return this.orderItemRepository.save(orderItem);
    }
}
