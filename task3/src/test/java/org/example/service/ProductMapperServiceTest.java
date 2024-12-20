package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.config.ObjectMapperConfig;
import org.example.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ObjectMapperConfig.class, ProductMapperService.class})
class ProductMapperServiceTest {

	@Autowired
	private ProductMapperService productMapperService;

	private Product testProduct;
	private List<Product> testProducts;

	@BeforeEach
	void setUp() {
		testProduct = new Product();
		testProduct.setProductId(1L);
		testProduct.setName("Test Product");
		testProduct.setDescription("Test Description");
		testProduct.setPrice(new BigDecimal("99.99"));
		testProduct.setQuantityInStock(10);

		Product product2 = new Product();
		product2.setProductId(2L);
		product2.setName("Test Product 2");
		product2.setPrice(new BigDecimal("199.99"));
		product2.setQuantityInStock(20);

		testProducts = Arrays.asList(testProduct, product2);
	}

	@Test
	void convertProductToJson_ShouldSerializeAllFields() {
		// When
		String json = productMapperService.convertProductToJson(testProduct);

		// Then
		assertTrue(json.contains("\"productId\":1"));
		assertTrue(json.contains("\"name\":\"Test Product\""));
		assertTrue(json.contains("\"price\":99.99"));
		assertTrue(json.contains("\"quantityInStock\":10"));
	}

	@Test
	void convertJsonToProduct_ShouldDeserializeAllFields() {
		// Given
		String json = productMapperService.convertProductToJson(testProduct);

		// When
		Product result = productMapperService.convertJsonToProduct(json);

		// Then
		assertEquals(testProduct.getProductId(), result.getProductId());
		assertEquals(testProduct.getName(), result.getName());
		assertEquals(testProduct.getPrice(), result.getPrice());
		assertEquals(testProduct.getQuantityInStock(), result.getQuantityInStock());
	}

	@Test
	void convertProductListToJson_ShouldSerializeAllProducts() {
		// When
		String json = productMapperService.convertProductListToJson(testProducts);

		// Then
		assertTrue(json.contains("\"productId\":1"));
		assertTrue(json.contains("\"productId\":2"));
		assertTrue(json.contains("\"name\":\"Test Product\""));
		assertTrue(json.contains("\"name\":\"Test Product 2\""));
	}

	@Test
	void convertJsonToProductList_ShouldDeserializeAllProducts() {
		// Given
		String json = productMapperService.convertProductListToJson(testProducts);

		// When
		List<Product> result = productMapperService.convertJsonToProductList(json);

		// Then
		assertEquals(2, result.size());
		assertEquals(testProducts.get(0).getProductId(), result.get(0).getProductId());
		assertEquals(testProducts.get(1).getProductId(), result.get(1).getProductId());
	}

	@Test
	void updateProductFromJson_ShouldUpdateOnlySpecifiedFields() {
		// Given
		String jsonPatch = "{\"name\":\"Updated Name\",\"price\":199.99}";

		// When
		Product result = productMapperService.updateProductFromJson(testProduct, jsonPatch);

		// Then
		assertEquals("Updated Name", result.getName());
		assertEquals(new BigDecimal("199.99"), result.getPrice());
		assertEquals(testProduct.getDescription(), result.getDescription());
		assertEquals(testProduct.getQuantityInStock(), result.getQuantityInStock());
	}

	@Test
	void convertProductToMap_ShouldContainAllFields() {
		// When
		Map<String, Object> result = productMapperService.convertProductToMap(testProduct);

		// Then
		assertEquals(1L, result.get("productId"));
		assertEquals("Test Product", result.get("name"));
		assertEquals(10, result.get("quantityInStock"));
	}

	@Test
	void convertMapToProduct_ShouldRestoreAllFields() {
		// Given
		Map<String, Object> map = productMapperService.convertProductToMap(testProduct);

		// When
		Product result = productMapperService.convertMapToProduct(map);

		// Then
		assertEquals(testProduct.getProductId(), result.getProductId());
		assertEquals(testProduct.getName(), result.getName());
		assertEquals(testProduct.getQuantityInStock(), result.getQuantityInStock());
	}

	@Test
	void convertJsonToProduct_WithInvalidJson_ShouldThrowException() {
		// Given
		String invalidJson = "{invalid:json}";

		// Then
		assertThrows(RuntimeException.class, () ->
				productMapperService.convertJsonToProduct(invalidJson)
		);
	}

	@Test
	void updateProductFromJson_WithInvalidJson_ShouldThrowException() {
		// Given
		String invalidJson = "{invalid:json}";

		// Then
		assertThrows(RuntimeException.class, () ->
				productMapperService.updateProductFromJson(testProduct, invalidJson)
		);
	}
}