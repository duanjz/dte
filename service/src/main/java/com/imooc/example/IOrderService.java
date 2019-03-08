package com.imooc.example;

import com.imooc.example.dto.OrderDTO;

public interface IOrderService {

    void create(OrderDTO dto);
    OrderDTO getMyOrder(Long id);
}
