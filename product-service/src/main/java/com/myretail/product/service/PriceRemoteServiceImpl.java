package com.myretail.product.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.myretail.product.dto.PriceDto;

@Service
public class PriceRemoteServiceImpl implements PriceRemoteService {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${product.price.url}")
	private String url;

	@Override
	public PriceDto apply(String id) {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("productId", id);

		// Query parameters
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		return restTemplate.getForEntity(builder.buildAndExpand(urlParams).toUriString(),PriceDto.class).getBody();
	}

}
