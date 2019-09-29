package com.myretail.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.myretail.product.dto.PriceDto;

@Service
public class PriceRemoteServiceImpl implements PriceRemoteService {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${product.price.url}")
	private String url;

	@Override
	public PriceDto apply(String id) {
		final String id_url = url.replace("{productId}", id);
		return restTemplate.getForEntity(id_url, PriceDto.class).getBody();
	}

}
