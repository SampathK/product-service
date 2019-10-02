package com.myretail.product.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.myretail.product.dto.PriceDto;
import com.myretail.product.exception.NotFoundException;
import com.myretail.product.model.Price;
import com.myretail.product.service.PriceRemoteService;
import com.myretail.product.service.PriceService;
import com.myretail.product.service.ProductNameRemoteService;



@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductNameRemoteService productNameRemoteService;
    
    @MockBean
    private PriceRemoteService priceRemoteService;
    
    @MockBean
    private PriceService priceService;

    @Test
    void given_noProduct_returnStatusNotFound() throws Exception {
    	
    	final String product = "13860428";
    	
    	given(productNameRemoteService.apply(product)).willReturn(null);
    	given(priceRemoteService.apply(product)).willReturn(null);
    	
    	ResultActions resultActions = mockMvc.perform(get("/api/products/"+product)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print());

        resultActions.andExpect(status().is4xxClientError()).andExpect(status().isNotFound());
    }
    
    @Test
    void given_product_noPrice_returnProductAndNAPrice() throws Exception {
        final String product = "13860428";
        final String product_name = "The Big Lebowski (Blu-ray)";
        given(productNameRemoteService.apply(product)).willReturn("The Big Lebowski (Blu-ray)");
    	given(priceRemoteService.apply(product)).willThrow(NotFoundException.class);
    	
    	ResultActions resultActions = mockMvc.perform(get("/api/products/"+product)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print());

        resultActions.andExpect(status().isOk()).andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json("{\"id\":\"13860428\",\"name\":\"The Big Lebowski (Blu-ray)\",\"current_price\":{\"value\":-1.0,\"currency_code\":\"N/A\"}}"))
        .andExpect(jsonPath("$.id", is(product)))
        .andExpect(jsonPath("$.name", is(product_name)))
        .andExpect(jsonPath("$.current_price.value", is(-1.0)))
        .andExpect(jsonPath("$.current_price.currency_code", is("N/A")));
    }
    
    
    @Test
    void given_product_Price_returnProductAndPrice() throws Exception {
        final String product = "13860428";
        final String product_name = "The Big Lebowski (Blu-ray)";
        given(productNameRemoteService.apply(product)).willReturn(product_name);
    	given(priceRemoteService.apply(product)).willReturn(new PriceDto(13.49,"USD"));
    	
    	ResultActions resultActions = mockMvc.perform(get("/api/products/"+product)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print());

        resultActions.andExpect(status().isOk()).andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json("{\"id\":\"13860428\",\"name\":\"The Big Lebowski (Blu-ray)\",\"current_price\":{\"value\":13.49,\"currency_code\":\"USD\"}}"))
        .andExpect(jsonPath("$.id", is(product)))
        .andExpect(jsonPath("$.name", is(product_name)))
        .andExpect(jsonPath("$.current_price.value", is(13.49)))
        .andExpect(jsonPath("$.current_price.currency_code", is("USD")));
    }
    
    @Test
    void given_noPrice_returnStatusNotFound() throws Exception {
    	
    	final String product = "13860428";
    	
    	given(priceService.getPriceByProductId(product)).willReturn(null);
    	
    	ResultActions resultActions = mockMvc.perform(get("/api/product/"+product+"/price")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print());

        resultActions.andExpect(status().is4xxClientError()).andExpect(status().isNotFound());
    }
    
    
    @Test
    void given_Price_returnStatusFound() throws Exception {
    	
    	final String product = "13860428";
    	
    	given(priceService.getPriceByProductId(product)).willReturn(new Price("111",product,13.49,"USD"));
    	
    	ResultActions resultActions = mockMvc.perform(get("/api/product/"+product+"/price")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print());

    	  resultActions.andExpect(status().isOk()).andExpect(status().is2xxSuccessful())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
          .andExpect(content().json("{\"value\":13.49,\"currency_code\":\"USD\"}"))
          .andExpect(jsonPath("$.value", is(13.49)))
          .andExpect(jsonPath("$.currency_code", is("USD")));
    }
    
    @Test
    void put_save_price_when_product_notfound() throws Exception {
    	final String product = "13860428";
        given(productNameRemoteService.apply(product)).willReturn(null);
        final String productContent = "{\"id\":\"13860428\",\"name\":\"The Big Lebowski (Blu-ray)\",\"current_price\":{\"value\":13.49,\"currency_code\":\"USD\"}}";
        ResultActions resultActions = mockMvc.perform(put("/api/products/{id}", product)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(productContent)).andDo(print());
        resultActions.andExpect(status().isBadRequest());

    }
    
    @Test
    void put_save_price_when_product_found() throws Exception {
    	final String product = "13860428";
        final String product_name = "The Big Lebowski (Blu-ray)";
        given(productNameRemoteService.apply(product)).willReturn(product_name);
    	given(priceService.getPriceByProductId(product)).willReturn(null);
        final Price curPrice = new Price("121", product, 14.49,"USD");
    	given(priceService.updateOrSavePrice(any(Price.class))).willReturn(curPrice);
    	
        final String productContent = "{\"id\":\"13860428\",\"name\":\"The Big Lebowski (Blu-ray)\",\"current_price\":{\"value\":14.49,\"currency_code\":\"USD\"}}";
        ResultActions resultActions = mockMvc.perform(put("/api/products/{id}", product)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(productContent)).andDo(print());
        resultActions.andExpect(status().isOk()).andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json("{\"id\":\"13860428\",\"name\":\"The Big Lebowski (Blu-ray)\",\"current_price\":{\"value\":14.49,\"currency_code\":\"USD\"}}"))
        .andExpect(jsonPath("$.id", is(product)))
        .andExpect(jsonPath("$.name", is(product_name)))
        .andExpect(jsonPath("$.current_price.value", is(14.49)))
        .andExpect(jsonPath("$.current_price.currency_code", is("USD")));

    }
    
    
    @Test
    void put_update_price_when_product_found() throws Exception {
    	final String product = "13860428";
        final String product_name = "The Big Lebowski (Blu-ray)";
        given(productNameRemoteService.apply(product)).willReturn(product_name);
        final Price price = new Price("121", product, 13.49,"USD");
    	given(priceService.getPriceByProductId(product)).willReturn(price);
        final Price curPrice = new Price("121", product, 14.49,"USD");
    	given(priceService.updateOrSavePrice(any(Price.class))).willReturn(curPrice);
    	
        final String productContent = "{\"id\":\"13860428\",\"name\":\"The Big Lebowski (Blu-ray)\",\"current_price\":{\"value\":14.49,\"currency_code\":\"USD\"}}";
        ResultActions resultActions = mockMvc.perform(put("/api/products/{id}", product)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(productContent)).andDo(print());
        resultActions.andExpect(status().isOk()).andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json("{\"id\":\"13860428\",\"name\":\"The Big Lebowski (Blu-ray)\",\"current_price\":{\"value\":14.49,\"currency_code\":\"USD\"}}"))
        .andExpect(jsonPath("$.id", is(product)))
        .andExpect(jsonPath("$.name", is(product_name)))
        .andExpect(jsonPath("$.current_price.value", is(14.49)))
        .andExpect(jsonPath("$.current_price.currency_code", is("USD")));

    }
    
    @Test
    void post_save_price_when_product_notfound() throws Exception {
    	final String product = "13860428";
        given(productNameRemoteService.apply(product)).willReturn(null);
        final String productContent = "{\"id\":\"13860428\",\"name\":\"The Big Lebowski (Blu-ray)\",\"current_price\":{\"value\":13.49,\"currency_code\":\"USD\"}}";
        ResultActions resultActions = mockMvc.perform(post("/api/products/{id}", product)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(productContent)).andDo(print());
        resultActions.andExpect(status().isBadRequest());

    }
    
    @Test
    void post_save_price_when_product_found() throws Exception {
    	final String product = "13860428";
        final String product_name = "The Big Lebowski (Blu-ray)";
        given(productNameRemoteService.apply(product)).willReturn(product_name);
    	given(priceService.getPriceByProductId(product)).willReturn(null);
        final Price curPrice = new Price("121", product, 14.49,"USD");
    	given(priceService.updateOrSavePrice(any(Price.class))).willReturn(curPrice);
    	
        final String productContent = "{\"id\":\"13860428\",\"name\":\"The Big Lebowski (Blu-ray)\",\"current_price\":{\"value\":14.49,\"currency_code\":\"USD\"}}";
        ResultActions resultActions = mockMvc.perform(post("/api/products/{id}", product)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(productContent)).andDo(print());
        resultActions.andExpect(status().isOk()).andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json("{\"id\":\"13860428\",\"name\":\"The Big Lebowski (Blu-ray)\",\"current_price\":{\"value\":14.49,\"currency_code\":\"USD\"}}"))
        .andExpect(jsonPath("$.id", is(product)))
        .andExpect(jsonPath("$.name", is(product_name)))
        .andExpect(jsonPath("$.current_price.value", is(14.49)))
        .andExpect(jsonPath("$.current_price.currency_code", is("USD")));

    }
    
    
    @Test
    void post_update_price_when_product_found() throws Exception {
    	final String product = "13860428";
        final String product_name = "The Big Lebowski (Blu-ray)";
        given(productNameRemoteService.apply(product)).willReturn(product_name);
        final Price price = new Price("121", product, 13.49,"USD");
    	given(priceService.getPriceByProductId(product)).willReturn(price);
        final Price curPrice = new Price("121", product, 14.49,"USD");
    	given(priceService.updateOrSavePrice(any(Price.class))).willReturn(curPrice);
    	
        final String productContent = "{\"id\":\"13860428\",\"name\":\"The Big Lebowski (Blu-ray)\",\"current_price\":{\"value\":14.49,\"currency_code\":\"USD\"}}";
        ResultActions resultActions = mockMvc.perform(post("/api/products/{id}", product)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(productContent)).andDo(print());
        resultActions.andExpect(status().isOk()).andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json("{\"id\":\"13860428\",\"name\":\"The Big Lebowski (Blu-ray)\",\"current_price\":{\"value\":14.49,\"currency_code\":\"USD\"}}"))
        .andExpect(jsonPath("$.id", is(product)))
        .andExpect(jsonPath("$.name", is(product_name)))
        .andExpect(jsonPath("$.current_price.value", is(14.49)))
        .andExpect(jsonPath("$.current_price.currency_code", is("USD")));

    }
}
