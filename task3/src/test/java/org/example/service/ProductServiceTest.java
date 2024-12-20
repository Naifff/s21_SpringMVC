package org.example.service;

import org.example.entity.Product;
import org.example.exception.BusinessException;
import org.example.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	private Product testProduct;

	@BeforeEach
	void setUp() {
		testProduct = new Product(1L, "Test Product", "Description", new BigDecimal("99.99"), 10);
	}

	@Test
	void getAllProducts_ShouldReturnListOfProducts() {
		when(productRepository.findAll()).thenReturn(Arrays.asList(testProduct));

		List<Product> result = productService.getAllProducts();

		assertFalse(result.isEmpty());
		assertEquals(1, result.size());
		assertEquals(testProduct.getName(), result.get(0).getName());
	}

	@Test
	void getProductById_WhenProductExists_ShouldReturnProduct() {
		when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

		Product result = productService.getProductById(1L);

		assertNotNull(result);
		assertEquals(testProduct.getName(), result.getName());
	}

	@Test
	void getProductById_WhenProductDoesNotExist_ShouldThrowException() {
		when(productRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(BusinessException.class, () -> productService.getProductById(1L));
	}

	@Test
	void createProduct_ShouldReturnSavedProduct() {
		when(productRepository.save(any(Product.class))).thenReturn(testProduct);

		Product result = productService.createProduct(testProduct);

		assertNotNull(result);
		assertEquals(testProduct.getName(), result.getName());
		verify(productRepository).save(any(Product.class));
	}
}