package com.myretail.product.model;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "price")
public class Price {
	@Id
	private String id;

	@Column(name = "product_id",nullable = false,unique = true)
	private String productId;

	@Column(name= "price",nullable = false,length = 12,precision = 2)
	private double value;
	
	@Column(name= "cur_code",nullable = false,length = 3)
	private String currency_code;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private ZonedDateTime createdOn;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", updatable = false)
	private ZonedDateTime updatedOn;

	public Price(String id, String product_id, double value,String currency_code) {
		this.id = id;
		this.productId = product_id;
		this.value = value;
		this.currency_code=currency_code;
	}
}
