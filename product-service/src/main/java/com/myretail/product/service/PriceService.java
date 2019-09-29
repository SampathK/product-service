package com.myretail.product.service;

import com.myretail.product.model.Price;

public interface PriceService {
   public Price getPriceByProductId(String productId);
   public Price updateOrSavePrice(Price price);
}
