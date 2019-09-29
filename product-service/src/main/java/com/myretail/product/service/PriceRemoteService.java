package com.myretail.product.service;

import java.util.function.Function;

import com.myretail.product.dto.PriceDto;

public interface PriceRemoteService extends Function<String, PriceDto> {

}
