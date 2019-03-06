package com.imooc.example.order.dao;

import com.imooc.example.order.domain.Order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findOneByCustomerId(Long customerId);
    
    Order findOneByUuid(String uuid);
}
