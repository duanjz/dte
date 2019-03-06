package com.imooc.example.ticket.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.example.ticket.domain.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findOneByOwner(Long owner);
    
    Ticket findOneByTicketNum(Long ticketNum);
    
    /**
     * 幂等操作
     * @param customerId
     * @param ticketNum
     * @return
     */
    @Modifying@Transactional
    @Query("UPDATE ticket SET lockUser = ?1 WHERE lockUser is NULL and ticketNum = ?2")
//    @Transactional
    public int lockTicket(Long customerId,Long ticketNum);
}
