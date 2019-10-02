package com.myretail.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.myretail.product.model.Price;
import com.myretail.product.repository.PriceRepository;


@ExtendWith(SpringExtension.class)
public class PriceServiceImplTests {
	@MockBean
	private PriceRepository priceRepository;
    
    private PriceService priceService;
    
    
    @BeforeEach
    void setUp() {
    	priceService = new PriceServiceImpl(priceRepository);
    }


    @Test
    void given_existingProductId_when_findPriceByProductId_then_returnPrice() {
        final String product = "13860428";
    	final Price price = new Price(UUID.randomUUID().toString(), product, 13.45, "USD");
        given(priceRepository.findPriceByProductId(product)).willReturn(price);
    	final Price returnedPrice = priceService.getPriceByProductId(product);
        assertThat(returnedPrice).isEqualToComparingFieldByField(price);
    }
    
    @Test
    void given_nonExistingProductId_when_findPriceByProductId_then_returnNull() {
        final String product = "13860429";
        given(priceRepository.findPriceByProductId(product)).willReturn(null);
    	final Price returnedPrice = priceService.getPriceByProductId(product);
        assertThat(returnedPrice).isNull();
    }
    
    @Test
    void given_Price_when_updateOrSavePrice_then_returnPrice() {
        final String product = "13860429";
    	final Price price = new Price(UUID.randomUUID().toString(), product, 14.45, "EUR");
        given(priceRepository.save(price)).willReturn(price);
    	final Price returnedPrice = priceService.updateOrSavePrice(price);
        assertThat(returnedPrice).isEqualToComparingFieldByField(price);
    }
    
}
