package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entity.Product;
import org.example.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@Autowired
	private ObjectMapper objectMapper;

	private Product testProduct;

	@BeforeEach
	void setUp() {
		// Initialize test data
		testProduct = new Product(1L, "Test Product", "Description", new BigDecimal("99.99"), 10);
	}

	@Test
	void getAllProducts_ShouldReturnListOfProducts() throws Exception {
		// Given
		when(productService.getAllProducts()).thenReturn(Arrays.asList(testProduct));

		// When & Then
		mockMvc.perform(get("/api/products"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].name").value(testProduct.getName()))
				.andExpect(jsonPath("$[0].price").value("99.99"));
	}

	@Test
	void getProductById_ShouldReturnProduct() throws Exception {
		// Given
		when(productService.getProductById(1L)).thenReturn(testProduct);

		// When & Then
		mockMvc.perform(get("/api/products/1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name").value(testProduct.getName()));
	}

	@Test
	void createProduct_ShouldReturnCreatedProduct() throws Exception {
		// Given
		when(productService.createProduct(any(Product.class))).thenReturn(testProduct);

		// When & Then
		mockMvc.perform(post("/api/products")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(testProduct)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value(testProduct.getName()));
	}

	@Test
	void updateProduct_ShouldReturnUpdatedProduct() throws Exception {
		// Given
		when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(testProduct);

		// When & Then
		mockMvc.perform(put("/api/products/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(testProduct)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value(testProduct.getName()));
	}

	@Test
	void deleteProduct_ShouldReturnNoContent() throws Exception {
		// When & Then
		mockMvc.perform(delete("/api/products/1"))
				.andExpect(status().isNoContent());
	}
}