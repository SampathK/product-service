package com.myretail.product.service;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.myretail.product.exception.InternalErrorException;

@Service
public class ProduceNameRemoteServiceImpl implements ProductNameRemoteService {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${product.name.url}")
	private String url;

	@Value("${product.name.cache.spec}")
	private String spec;

	private LoadingCache<String, String> productCache;
	
	@PostConstruct
	public void init() {
		this.productCache = CacheBuilder.from(spec).build(new CacheLoader<String, String>() {
			@Override
			public String load(String id) throws Exception {
				return getProductName(id);
			}
		});
	}

	@Override
	public String apply(String id) {
		String productName = null;
		try {
			productName = productCache.get(id);
			if(StringUtils.isEmpty(productName)) {
				return null;
			}
			return productName;
		} catch (Exception ex) {
           final String message = String.format("Error %s in processing the request for %s ",ex.getMessage(),id);
           throw new InternalErrorException(message);
		}
	}

	private String getProductName(String id) throws IOException {
		final String id_url = url.replace("{id}", id);
		final String productResponse = restTemplate.getForEntity(id_url, String.class).getBody();
		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode treeNode = mapper.readValue(productResponse, ObjectNode.class);
		if (treeNode.has("product")) {
			JsonNode productNode = treeNode.get("product");
			if (productNode.has("item")) {
				JsonNode itemNode = productNode.get("item");
				if (itemNode.has("product_description")) {
					JsonNode descNode = itemNode.get("product_description");
					if (descNode.has("title")) {
						return descNode.get("title").asText().trim();
					}
				}
			}
		}
		return "";
	}

}
