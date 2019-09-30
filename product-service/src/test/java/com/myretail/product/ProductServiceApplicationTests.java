package com.myretail.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.myretail.product.controller.ProductController;
import com.myretail.product.service.PriceRemoteService;
import com.myretail.product.service.PriceService;
import com.myretail.product.service.ProductNameRemoteService;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ProductServiceApplicationTests {

	@Autowired
	private ProductController productController;

	@Autowired
	private PriceRemoteService priceRemoteService;

	@Autowired
	private PriceService priceService;

	@Autowired
	private ProductNameRemoteService productNameRemoteService;

	@Test
	public void contextLoads() {
		assertThat(productController).isNotNull();
		assertThat(priceRemoteService).isNotNull();
		assertThat(priceService).isNotNull();
		assertThat(productNameRemoteService).isNotNull();

	}

}
