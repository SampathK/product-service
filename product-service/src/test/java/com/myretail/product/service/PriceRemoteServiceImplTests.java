package com.myretail.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.myretail.product.dto.PriceDto;
import com.myretail.product.exception.NotFoundException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PriceRemoteServiceImplTests {

	@MockBean
	private RestTemplate restTemplate;

	@Autowired
	private PriceRemoteService priceRemoteService;

	@Value("${product.price.url}")
	private String url;


	@Test
	void price_avail_then_returnPrice() {
		final String product = "13860428";

		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("productId", product);

		// Query parameters
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		given(restTemplate.getForEntity(builder.buildAndExpand(urlParams).toUriString(), PriceDto.class))
				.willReturn(new ResponseEntity<PriceDto>(new PriceDto(13.35, "USD"), HttpStatus.OK));
		assertThat(priceRemoteService.apply(product))
				.isEqualToComparingFieldByField(new PriceDto(13.35, "USD"));

	}
	
	@Test
	void price_notavail_then_returnPrice() {
		final String product = "13860428";

		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("productId", product);

		// Query parameters
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		given(restTemplate.getForEntity(builder.buildAndExpand(urlParams).toUriString(), PriceDto.class))
				.willThrow(NotFoundException.class);
		 assertThrows(NotFoundException.class, () -> {
		        priceRemoteService.apply(product);
		    });		

	}

}
