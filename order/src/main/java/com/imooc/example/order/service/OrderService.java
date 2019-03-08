package com.imooc.example.order.service;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.example.dto.OrderDTO;
import com.imooc.example.order.dao.OrderRepository;
import com.imooc.example.order.domain.Order;

@Service
public class OrderService {
	private static final Logger LOG = LoggerFactory.getLogger(OrderService.class);
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private JmsTemplate jmsTemplate;
	
	/**
	 * 锁票成功后付款
	 * @param dto
	 */
	@Transactional
	@JmsListener(destination="order:locked",containerFactory="msgFactory")
	public void handleOderNew(OrderDTO dto){
		LOG.info("Get new order to create:{}",dto);
		if(orderRepository.findOneByUuid(dto.getUuid()) != null){//订单已存在
			LOG.info("Msg already created:{}",dto);
		}else{
			Order order = createOrder(dto);
			orderRepository.save(order);
			dto.setId(order.getId());
		}
		dto.setStatus("NEW");
		jmsTemplate.convertAndSend("order:pay",dto);
	}
	
	private Order createOrder(OrderDTO dto){
		Order order = new Order();
		order.setUuid(dto.getUuid());
		order.setAmount(dto.getAmount());
		order.setTitle(dto.getTitle());
		order.setCustomerId(dto.getCustomerId());
		order.setTicketNum(dto.getTicketNum());
		order.setStatus("NEW");
		order.setCreateTime(ZonedDateTime.now());
		return order;
	}
	
	@Transactional
	@JmsListener(destination="order:finish",containerFactory="msgFactory")
	public void handleOderFinish(OrderDTO dto){
		LOG.info("Get new order to finish:{}",dto);
		Order order = orderRepository.findOne(dto.getId());
		dto.setStatus("FINISH");
		orderRepository.save(order);
	}
	
}
