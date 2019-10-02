package com.myretail.product.service;

import static org.assertj.core.api.Assertions.assertThat;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProduceNameRemoteServiceImplTests {
	@MockBean
	private RestTemplate restTemplate;

	@Autowired
	private ProductNameRemoteService productNameRemoteService;

	@Value("${product.name.url}")
	private String url;

	@Test
	void product_avail_then_returnProductName() {
		final String product = "13860431";

		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("id", product);
		final String productContent = "{\"product\":{\"deep_red_labels\":{\"total_count\":2,\"labels\":[{\"id\":\"twbl94\",\"name\":\"Movies\",\"type\":\"merchandise type\",\"priority\":0,\"count\":1},{\"id\":\"rv3fdu\",\"name\":\"SA\",\"type\":\"relationship type\",\"priority\":0,\"count\":1}]},\"available_to_promise_network\":{\"product_id\":\"13860431\",\"id_type\":\"TCIN\",\"available_to_promise_quantity\":0.0,\"street_date\":\"2013-03-05T06:00:00.000Z\",\"availability\":\"UNAVAILABLE\",\"online_available_to_promise_quantity\":0.0,\"stores_available_to_promise_quantity\":0.0,\"availability_status\":\"OUT_OF_STOCK\",\"multichannel_options\":[],\"is_infinite_inventory\":false,\"loyalty_availability_status\":\"OUT_OF_STOCK\",\"loyalty_purchase_start_date_time\":\"1970-01-01T00:00:00.000Z\",\"is_loyalty_purchase_enabled\":false,\"is_out_of_stock_in_all_store_locations\":true,\"is_out_of_stock_in_all_online_locations\":true},\"item\":{\"tcin\":\"13860431\",\"dpci\":\"246-07-4229\",\"upc\":\"031398147084\",\"product_description\":{\"title\":\"The Hunters (DVD)\",\"downstream_description\":\"Alice (Dianna Agron) and her friends are approaching the end of the school year, when their dead-end lives will end and the chance of a new life will begin. Before heading off to college, they spend one last day together in the woods, the one part of town that has always been off-limits to them growing up. As they stumble upon what they thought was an abandoned fort, only to find the walls dripping in blood and decomposing body parts lying around, they are startled to learn they are now a part of an undercover investigation. After being told to get out of the woods, they realize they're trapped, for The Hunters, who call the fort home, never let anyone out alive!\",\"bullet_description\":[\"<B>Movie MPAA Rating:</B> R\",\"<B>Movie Studio:</B> Lionsgate\",\"<B>Movie Genre:</B> Horror, Film + TV + Radio\",\"<B>Software Format:</B> DVD\",\"<B>Language:</B> English\"]},\"buy_url\":\"https://www.target.com/p/the-hunters-dvd/-/A-13860431\",\"enrichment\":{\"images\":[{\"base_url\":\"https://target.scene7.com/is/image/Target/\",\"primary\":\"GUEST_754e9e16-b6e0-4699-ab7f-eb952ac88dd7\",\"content_labels\":[{\"image_url\":\"GUEST_754e9e16-b6e0-4699-ab7f-eb952ac88dd7\"}]}],\"sales_classification_nodes\":[{\"node_id\":\"5xswt\"},{\"node_id\":\"55uhs\"},{\"node_id\":\"5xsws\"}]},\"return_method\":\"This item can be returned to any Target store or Target.com.\",\"handling\":{},\"recall_compliance\":{\"is_product_recalled\":false},\"tax_category\":{\"tax_code_id\":99999,\"tax_code\":\"99999\"},\"display_option\":{\"is_size_chart\":false},\"fulfillment\":{\"is_po_box_prohibited\":true,\"po_box_prohibited_message\":\"We regret that this item cannot be shipped to PO Boxes.\",\"box_percent_filled_by_volume\":0.02,\"box_percent_filled_by_weight\":0.4,\"box_percent_filled_display\":0.4},\"package_dimensions\":{\"weight\":\"0.17\",\"weight_unit_of_measure\":\"POUND\",\"width\":\"1.0\",\"depth\":\"1.0\",\"height\":\"1.0\",\"dimension_unit_of_measure\":\"INCH\"},\"environmental_segmentation\":{},\"manufacturer\":{},\"product_vendors\":[{\"id\":\"1984811\",\"manufacturer_style\":\"031398147084\",\"vendor_name\":\"Ingram Entertainment\"}],\"product_classification\":{\"product_type\":\"542\",\"product_type_name\":\"ELECTRONICS\",\"item_type_name\":\"Movies\",\"item_type\":{\"category_type\":\"Item Type: MMBV\",\"type\":300752,\"name\":\"movies\"}},\"product_brand\":{},\"item_state\":\"READY_FOR_LAUNCH\",\"specifications\":[],\"mpaa_rating\":{\"url\":\"https://Img1.targetimg1.com/sites/html/TargetOnline/help/common/Movie_R_Rating.html\",\"image\":\"https://Img1.targetimg1.com/wcsstore/TargetSAS/images/109776_R.jpg\",\"rating\":\"r\"},\"attributes\":{\"gift_wrapable\":\"N\",\"has_prop65\":\"N\",\"is_hazmat\":\"N\",\"max_order_qty\":10,\"street_date\":\"2013-03-05\",\"media_format\":\"DVD\",\"merch_class\":\"TCOM MOVIES 1\",\"merch_classid\":246,\"merch_subclass\":7,\"return_method\":\"This item can be returned to any Target store or Target.com.\",\"ship_to_restriction\":\"United States Minor Outlying Islands,American Samoa (see also separate entry under AS),Puerto Rico (see also separate entry under PR),Northern Mariana Islands,Virgin Islands, U.S.,APO/FPO,Guam (see also separate entry under GU)\"},\"country_of_origin\":\"US\",\"relationship_type_code\":\"Stand Alone\",\"subscription_eligible\":false,\"ribbons\":[],\"tags\":[],\"ship_to_restriction\":\"This item cannot be shipped to the following locations: United States Minor Outlying Islands, American Samoa, Puerto Rico, Northern Mariana Islands, Virgin Islands, U.S., APO/FPO, Guam\",\"estore_item_status_code\":\"A\",\"return_policies\":{\"user\":\"Regular Guest\",\"policyDays\":\"90\",\"guestMessage\":\"This item must be returned within 90 days of the ship date. See return policy for details.\"},\"gifting_enabled\":false}}}";
		// Query parameters
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		given(restTemplate.getForEntity(builder.buildAndExpand(urlParams).toUriString(), String.class))
				.willReturn(new ResponseEntity<String>(productContent, HttpStatus.OK));
		assertThat(productNameRemoteService.apply(product)).isEqualTo("The Hunters (DVD)");
	}
	
	@Test
	void product_not_avail_then_return_null() {
		final String product = "1386044";

		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("id", product);
		final String productContent = "{\"product\":{\"item\":{}}}";
		// Query parameters
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		given(restTemplate.getForEntity(builder.buildAndExpand(urlParams).toUriString(), String.class))
				.willReturn(new ResponseEntity<String>(productContent, HttpStatus.OK));
		assertThat(productNameRemoteService.apply(product)).isNull();

	}

}
