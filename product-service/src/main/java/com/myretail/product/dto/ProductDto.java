package com.myretail.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
  
  private String id;
  
  private String name;
  
  private PriceDto current_price;
  
}
