package com.imooc.example.ticket.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.example.dto.OrderDTO;
import com.imooc.example.ticket.dao.TicketRepository;
import com.imooc.example.ticket.domain.Ticket;

@Service
public class TicketService {
	private static final Logger LOG = LoggerFactory.getLogger(TicketService.class);
	@Autowired
	private TicketRepository ticketRepository;
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Transactional
	@JmsListener(destination="order:new",containerFactory="msgFactory")
	public void handleTicketLock(OrderDTO dto){
		System.out.println("1111111111111111111");
		int count = ticketRepository.lockTicket(dto.getCustomerId(), dto.getTicketNum());
		LOG.info("Get new order for ticket lock:{}",dto);
		if(count == 1){
			dto.setStatus("TICKET_LOCKED");
			jmsTemplate.convertAndSend("order:lock",dto);
		}else{
			
		}
	}
	
//	/**
//	 * 先查询数据，再做更改，不能满足多线程同步的要求
//	 */
//	@Transactional
//	public Ticket ticketLock(OrderDTO dto){
//		Ticket ticket = ticketRepository.findOneByTicketNum(dto.getTicketNum());
//		ticket.setLockUser(dto.getCustomerId());
//		ticket = ticketRepository.save(ticket);
//		
//		try {
//			Thread.sleep(10 * 1000);
//		} catch (InterruptedException e) {
//			LOG.error(e.getMessage());
//		}
//		
//		return ticket;
//	}
	
	@Transactional
	public int ticketLock2(OrderDTO dto){
		int lockCount = ticketRepository.lockTicket(dto.getCustomerId(), dto.getTicketNum());
		LOG.info("Update ticket lock count:{}",lockCount);
		
		try {
			Thread.sleep(10 * 1000);
		} catch (InterruptedException e) {
			LOG.error(e.getMessage());
		}
		
		return lockCount;
	}
	
}
