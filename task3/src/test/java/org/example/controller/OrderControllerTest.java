package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entity.Customer;
import org.example.entity.Order;
import org.example.entity.OrderStatus;
import org.example.entity.Product;
import org.example.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderService orderService;

	@Autowired
	private ObjectMapper objectMapper;

	private Order testOrder;
	private Customer testCustomer;
	private Product testProduct;

	@BeforeEach
	void setUp() {
		// Initialize test data
		testCustomer = new Customer(1L, "John", "Doe", "john@example.com", "1234567890");
		testProduct = new Product(1L, "Test Product", "Description", new BigDecimal("99.99"), 10);
		testOrder = new Order(1L, testCustomer, Arrays.asList(testProduct),
				LocalDateTime.now(), "123 Test St", new BigDecimal("99.99"), OrderStatus.PENDING);
	}

	@Test
	void getAllOrders_ShouldReturnListOfOrders() throws Exception {
		// Given
		when(orderService.getAllOrders()).thenReturn(Arrays.asList(testOrder));

		// When & Then
		mockMvc.perform(get("/api/orders"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].orderId").value(testOrder.getOrderId()))
				.andExpect(jsonPath("$[0].orderStatus").value(testOrder.getOrderStatus().toString()));
	}

	@Test
	void getOrderById_ShouldReturnOrder() throws Exception {
		// Given
		when(orderService.getOrderById(1L)).thenReturn(testOrder);

		// When & Then
		mockMvc.perform(get("/api/orders/1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.orderId").value(testOrder.getOrderId()));
	}

	@Test
	void createOrder_ShouldReturnCreatedOrder() throws Exception {
		// Given
		when(orderService.createOrder(any(Order.class))).thenReturn(testOrder);

		// When & Then
		mockMvc.perform(post("/api/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(testOrder)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.orderId").value(testOrder.getOrderId()))
				.andExpect(jsonPath("$.orderStatus").value(testOrder.getOrderStatus().toString()));
	}
}