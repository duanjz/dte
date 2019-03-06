package com.imooc.example.ticket.web;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.example.dto.OrderDTO;
import com.imooc.example.ticket.dao.TicketRepository;
import com.imooc.example.ticket.domain.Ticket;
import com.imooc.example.ticket.service.TicketService;

@RestController
@RequestMapping("/api/ticket")
public class TicketResource{
    @Autowired
    private TicketRepository ticketRepository;
    
    @PostConstruct
    public void init() {
    	if(ticketRepository.count() > 0){
    		return;
    	}
        Ticket ticket = new Ticket();
        ticket.setName("Num.1");
        ticket.setTicketNum(100L);
        ticketRepository.save(ticket);
    }

    @Autowired
    private TicketService ticketService;
    
//    @PostMapping("/lock")
//    public Ticket lock(@RequestBody OrderDTO dto){
//    	return ticketService.ticketLock(dto);
//    }
    
    @PostMapping("/lock2")
    public Integer lock2(@RequestBody OrderDTO dto){
    	return ticketService.ticketLock2(dto);
    }
    

}
