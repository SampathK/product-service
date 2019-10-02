package com.myretail.product.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.myretail.product.model.Price;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class PriceRepositoryTests {
	    
	    @Autowired
	    private TestEntityManager entityManager;

	    @Autowired
	    private PriceRepository priceRepository;

	    @Test
	    void given_existingProductId_when_findPriceByProductId_then_returnPrice() {
	        final String product = "13860428";
	    	final Price price = new Price(UUID.randomUUID().toString(), product, 13.45, "USD");
	        entityManager.persist(price);
            final Price updatedPrice = priceRepository.findPriceByProductId(product);
	        assertThat(updatedPrice).isEqualToComparingFieldByField(price);
	    }
	    
	    @Test
	    void given_nonExistingProductId_when_findPriceByProductId_then_returnNull() {
	        final String product = "13860429";
            final Price updatedPrice = priceRepository.findPriceByProductId(product);
	        assertThat(updatedPrice).isNull();
	    }

}
