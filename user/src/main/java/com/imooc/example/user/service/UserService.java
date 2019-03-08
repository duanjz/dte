package com.imooc.example.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.example.dto.OrderDTO;
import com.imooc.example.user.dao.CustomerRepository;
import com.imooc.example.user.dao.PayInfoRepository;
import com.imooc.example.user.domain.Customer;
import com.imooc.example.user.domain.PayInfo;

@Service
public class UserService {
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private PayInfoRepository payInfoRepository;
	
	/**
	 * 订单生成后付款
	 * @param dto
	 */
	@Transactional
	@JmsListener(destination="order:pay",containerFactory="msgFactory")
	public void handleOderPay(OrderDTO dto){
		LOG.info("Get new order to pay:{}",dto);
		PayInfo payInfo = payInfoRepository.findOneByOrderId(dto.getId());
		Customer customer = customerRepository.findOne(dto.getCustomerId());
		//存在疑问
		if(payInfo != null){//订单已支付完成
			LOG.warn("order already pay:{}",dto);
			return;
		}else{
			if(customer.getDeposit() < dto.getAmount()){//余额不够支付
				return;
			}
			payInfo = new PayInfo();
			payInfo.setOrderId(dto.getId());
			payInfo.setAmount(dto.getAmount());
			payInfo.setStatus("PAID");
			payInfoRepository.save(payInfo);
			//扣款操作
			customerRepository.charge(dto.getCustomerId(), dto.getAmount());
		}
		
		dto.setStatus("PAID");
		jmsTemplate.convertAndSend("order:ticket_move",dto);//进行票的转移
	}
	
}
