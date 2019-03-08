package com.imooc.example.order.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.imooc.example.IOrderService;
import com.imooc.example.dto.OrderDTO;
import com.imooc.example.order.dao.OrderRepository;
import com.imooc.example.order.domain.Order;

@RestController
@RequestMapping("/api/order")
public class OrderResource implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private JmsTemplate jmsTemplate;
    
    private TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator();

    @PostMapping("/create")
    public void create(@RequestBody OrderDTO dto) {//发起订单
    	dto.setUuid(uuidGenerator.generate().toString());
    	jmsTemplate.convertAndSend("order:new",dto);
    }

    @GetMapping("/{id}")
    public OrderDTO getMyOrder(@PathVariable Long id) {
        Order order = orderRepository.findOne(id);
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setAmount(order.getAmount());
        dto.setTitle(order.getTitle());
        return dto;
    }

    @GetMapping("")
    public List<Order> getAll() {
        return orderRepository.findAll();
    }
    
    

}
