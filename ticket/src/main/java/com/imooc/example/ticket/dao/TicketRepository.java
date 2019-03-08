package com.imooc.example.ticket.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.imooc.example.ticket.domain.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findOneByOwner(Long owner);
    
    Ticket findOneByTicketNumAndLockUser(Long ticketNum,Long lockUser);
    
    /**
     * 幂等操作
     * @param customerId
     * @param ticketNum
     * @return
     */
    @Modifying
    @Query("UPDATE ticket SET lockUser = ?1 WHERE lockUser is NULL and ticketNum = ?2")
    public int lockTicket(Long customerId,Long ticketNum);
    
    @Modifying
    @Query("UPDATE ticket SET owner = ?1, lockUser = NULL WHERE lockUser = ?1 and ticketNum = ?2")
    public int moveTicket(Long customerId,Long ticketNum);
    
    
}
