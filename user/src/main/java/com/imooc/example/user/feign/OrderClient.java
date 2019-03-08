package com.imooc.example.user.feign;

import com.imooc.example.IOrderService;
import com.imooc.example.dto.OrderDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "order", path = "/api/order")
public interface OrderClient extends IOrderService {

    @GetMapping("/{id}")
    OrderDTO getMyOrder(@PathVariable(name = "id") Long id);

    @PostMapping("")
    void create(@RequestBody OrderDTO dto);
}
