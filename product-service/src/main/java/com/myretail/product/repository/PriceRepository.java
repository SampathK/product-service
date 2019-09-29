package com.myretail.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myretail.product.model.Price;

public interface PriceRepository extends JpaRepository<Price, String> {
  
	Price findPriceByProductId(String productId);
    
}
