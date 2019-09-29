package com.myretail.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.myretail.product.exception.RestTemplateResponseErrorHandler;

@Configuration
public class RestTemplateConfig {

	@Bean
	RestTemplate restTemplate() {
		final RestTemplate template = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
		template.setErrorHandler(new RestTemplateResponseErrorHandler());
		return template;
	}

}
