package com.imooc.example.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imooc.example.user.domain.PayInfo;

public interface PayInfoRepository extends JpaRepository<PayInfo, Long> {

    PayInfo findOneByOrderId(Long orderId);
}
