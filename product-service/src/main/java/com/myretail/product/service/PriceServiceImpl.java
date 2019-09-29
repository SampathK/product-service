package com.myretail.product.service;

import org.springframework.stereotype.Service;

import com.myretail.product.model.Price;
import com.myretail.product.repository.PriceRepository;

@Service
public class PriceServiceImpl implements PriceService {

	private final PriceRepository priceRepository;
	
	public PriceServiceImpl(PriceRepository priceRepository) {
		this.priceRepository = priceRepository;
	}
	
	@Override
	public Price getPriceByProductId(String productId) {
		final Price price = priceRepository.findPriceByProductId(productId);
		return price;
	}

	@Override
	public Price updateOrSavePrice(Price price) {
		return priceRepository.save(price);
	}

}
