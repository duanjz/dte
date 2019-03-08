package com.imooc.example.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.imooc.example.user.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findOneByUsername(String username);
    
    @Modifying
    @Query("UPDATE customer SET deposit = deposit - ?2 WHERE id = ?1")
    int charge(Long customerId,int amount);
}
