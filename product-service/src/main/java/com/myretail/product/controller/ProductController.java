package com.myretail.product.controller;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import javax.validation.Valid;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.myretail.product.dto.PriceDto;
import com.myretail.product.dto.ProductDto;
import com.myretail.product.exception.BadRequestException;
import com.myretail.product.exception.InternalErrorException;
import com.myretail.product.exception.NotFoundException;
import com.myretail.product.model.Price;
import com.myretail.product.service.PriceRemoteService;
import com.myretail.product.service.PriceService;
import com.myretail.product.service.ProductNameRemoteService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class ProductController {

	private final ProductNameRemoteService productNameRemoteService;
	private final PriceRemoteService priceRemoteService;
	private final PriceService priceService;

	public ProductController(ProductNameRemoteService productNameRemoteService, PriceRemoteService priceRemoteService,
			PriceService priceService) {
		this.productNameRemoteService = productNameRemoteService;
		this.priceRemoteService = priceRemoteService;
		this.priceService = priceService;
	}

	@GetMapping("/products/{id}")
	public ProductDto getProductById(@PathVariable String id) {
		log.info("Get Product with id '{}'", id);
		final CompletableFuture<NotFoundException> notFoundException = new CompletableFuture<>();
		final CompletableFuture<InternalErrorException> internalException = new CompletableFuture<>();
		final CompletableFuture<String> nameFuture = getProductNameFuture(id, notFoundException, internalException);
		final CompletableFuture<PriceDto> priceFuture = getPriceFuture(id,internalException);
		final CompletableFuture<ProductDto> producFuture = CompletableFuture.allOf(nameFuture, priceFuture)
				.thenApply(ignoredVoid -> combine(id, nameFuture.join(), priceFuture.join()));
		return getProduct(id, notFoundException, internalException, producFuture);
	}

	@GetMapping("/product/{productId}/price")
	public PriceDto getPriceById(@PathVariable String productId) {
		final Price price = priceService.getPriceByProductId(productId);
		if(null == price) {
			final String message = String.format("Price is not found for product Id %s", productId);
			throw new NotFoundException(message);
		}
		final PriceDto priceDto = new PriceDto(price.getValue(), price.getCurrency_code());
		return priceDto;
	}

	@RequestMapping(value={"/products/{id}"},method={RequestMethod.POST,RequestMethod.PUT})
	public ProductDto updateOrSavePrice(@PathVariable String id, @Valid @RequestBody ProductDto productDto) {
		final String productName = productNameRemoteService.apply(id);
		if(StringUtils.isEmpty(productName)) {
			final String message = String.format("Product name %s is not available", id);
			throw new BadRequestException(message);
		}
		Price price = priceService.getPriceByProductId(id);
		if(null == price) {
			price = new Price();
			price.setId(UUID.randomUUID().toString());
			price.setProductId(id);
		}
		PriceDto priceDto = productDto.getCurrent_price();
		price.setValue(priceDto.getValue());
		price.setCurrency_code(priceDto.getCurrency_code());
		price = priceService.updateOrSavePrice(price);
		priceDto = new PriceDto(price.getValue(),price.getCurrency_code());
		return combine(id, productName, priceDto);
	}
	
	private ProductDto getProduct(String id, CompletableFuture<NotFoundException> notFoundException,
			CompletableFuture<InternalErrorException> internalException, CompletableFuture<ProductDto> producFuture) {
		try {
			return producFuture.join();
		} catch (CompletionException cex) {
			log.error("Error in getting produce details for {}", id, cex);
			if (notFoundException.isDone())
				throw notFoundException.join();
			if (internalException.isDone())
				throw internalException.join();
			final Throwable cause = cex.getCause();
			final String message = String.format("Error %s in processing the request for %s", id, cause.getMessage());
			throw new InternalErrorException(message);
		}
	}

	private CompletableFuture<PriceDto> getPriceFuture(String id,
			CompletableFuture<InternalErrorException> internalException) {
		final CompletableFuture<PriceDto> priceFuture = CompletableFuture.supplyAsync(() -> {
			try {
				return priceRemoteService.apply(id);
			} catch (NotFoundException ex) {
				return new PriceDto(-1.00, "N/A");
			} catch (InternalErrorException ex) {
				internalException.complete(ex);
				throw new CompletionException(ex);
			}
		});
		return priceFuture;
	}

	private CompletableFuture<String> getProductNameFuture(String id,
			CompletableFuture<NotFoundException> notFoundException,
			CompletableFuture<InternalErrorException> internalException) {
		final CompletableFuture<String> nameFuture = CompletableFuture.supplyAsync(() -> {
			try {
				final String productName = productNameRemoteService.apply(id);
				if (StringUtils.isEmpty(productName)) {
					final String message = String.format("Product name %s is not available", id);
					throw new NotFoundException(message);
				}
				return productName;
			} catch (NotFoundException ex) {
				notFoundException.complete(ex);
				throw new CompletionException(ex);
			} catch (InternalErrorException ex) {
				internalException.complete(ex);
				throw new CompletionException(ex);
			}
		});
		return nameFuture;
	}

	private ProductDto combine(String productId, String productName, PriceDto price) {
		return new ProductDto(productId, productName, price);
	}

}
