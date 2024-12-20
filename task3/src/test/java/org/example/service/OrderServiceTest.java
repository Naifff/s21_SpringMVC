package org.example.service;

import org.example.entity.Customer;
import org.example.entity.Order;
import org.example.entity.OrderStatus;
import org.example.entity.Product;
import org.example.exception.BusinessException;
import org.example.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private ProductService productService;

	@InjectMocks
	private OrderService orderService;

	private Order testOrder;
	private Product testProduct;
	private Customer testCustomer;

	@BeforeEach
	void setUp() {
		// Initialize test data
		testCustomer = new Customer(1L, "John", "Doe", "john@example.com", "1234567890");
		testProduct = new Product(1L, "Test Product", "Description", new BigDecimal("99.99"), 10);
		testOrder = new Order(1L, testCustomer, Arrays.asList(testProduct),
				LocalDateTime.now(), "123 Test St", new BigDecimal("99.99"), OrderStatus.PENDING);
	}

	@Test
	void getAllOrders_ShouldReturnListOfOrders() {
		// Given
		when(orderRepository.findAll()).thenReturn(Arrays.asList(testOrder));

		// When
		List<Order> result = orderService.getAllOrders();

		// Then
		assertFalse(result.isEmpty());
		assertEquals(1, result.size());
		assertEquals(testOrder.getOrderId(), result.get(0).getOrderId());
	}

	@Test
	void getOrderById_WhenOrderExists_ShouldReturnOrder() {
		// Given
		when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

		// When
		Order result = orderService.getOrderById(1L);

		// Then
		assertNotNull(result);
		assertEquals(testOrder.getOrderId(), result.getOrderId());
	}

	@Test
	void getOrderById_WhenOrderDoesNotExist_ShouldThrowException() {
		// Given
		when(orderRepository.findById(1L)).thenReturn(Optional.empty());

		// When & Then
		assertThrows(BusinessException.class, () -> orderService.getOrderById(1L));
	}

	@Test
	void createOrder_WithValidData_ShouldReturnSavedOrder() {
		// Given
		when(productService.getProductById(1L)).thenReturn(testProduct);
		when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

		// When
		Order result = orderService.createOrder(testOrder);

		// Then
		assertNotNull(result);
		assertEquals(OrderStatus.PENDING, result.getOrderStatus());
		verify(orderRepository).save(any(Order.class));
	}

	@Test
	void createOrder_WithOutOfStockProduct_ShouldThrowException() {
		// Given
		Product outOfStockProduct = new Product(1L, "Test Product", "Description",
				new BigDecimal("99.99"), 0);
		when(productService.getProductById(1L)).thenReturn(outOfStockProduct);

		// When & Then
		assertThrows(BusinessException.class, () -> orderService.createOrder(testOrder));
	}
}