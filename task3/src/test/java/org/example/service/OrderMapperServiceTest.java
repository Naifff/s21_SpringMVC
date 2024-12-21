package org.example.service;

import org.example.config.JacksonConfig;
import org.example.entity.Customer;
import org.example.entity.Order;
import org.example.entity.OrderStatus;
import org.example.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JacksonConfig.class, OrderMapperService.class})
class OrderMapperServiceTest {

	@Autowired
	private OrderMapperService orderMapperService;

	private Order testOrder;
	private List<Order> testOrders;
	private Customer testCustomer;
	private Product testProduct1;
	private Product testProduct2;
	private LocalDateTime testDateTime;

	@BeforeEach
	void setUp() {
		// Initialize a fixed datetime for consistent testing
		testDateTime = LocalDateTime.of(2024, 1, 1, 12, 0);

		// Set up test customer
		testCustomer = new Customer();
		testCustomer.setCustomerId(1L);
		testCustomer.setFirstName("John");
		testCustomer.setLastName("Doe");
		testCustomer.setEmail("john@example.com");
		testCustomer.setContactNumber("1234567890");

		// Set up test products
		testProduct1 = new Product();
		testProduct1.setProductId(1L);
		testProduct1.setName("Test Product 1");
		testProduct1.setPrice(new BigDecimal("99.99"));
		testProduct1.setQuantityInStock(10);

		testProduct2 = new Product();
		testProduct2.setProductId(2L);
		testProduct2.setName("Test Product 2");
		testProduct2.setPrice(new BigDecimal("149.99"));
		testProduct2.setQuantityInStock(5);

		// Set up primary test order
		testOrder = new Order();
		testOrder.setOrderId(1L);
		testOrder.setCustomer(testCustomer);
		testOrder.setProducts(Arrays.asList(testProduct1, testProduct2));
		testOrder.setOrderDate(testDateTime);
		testOrder.setShippingAddress("123 Test St");
		testOrder.setTotalPrice(new BigDecimal("249.98"));
		testOrder.setOrderStatus(OrderStatus.PENDING);

		// Set up second order for list testing
		Order order2 = new Order();
		order2.setOrderId(2L);
		order2.setCustomer(testCustomer);
		order2.setProducts(Arrays.asList(testProduct1));
		order2.setOrderDate(testDateTime.plusDays(1));
		order2.setShippingAddress("456 Test Ave");
		order2.setTotalPrice(new BigDecimal("99.99"));
		order2.setOrderStatus(OrderStatus.CONFIRMED);

		testOrders = Arrays.asList(testOrder, order2);
	}

	@Test
	void convertOrderToJson_ShouldSerializeAllFields() {
		// When
		String json = orderMapperService.convertOrderToJson(testOrder);

		// Then
		assertTrue(json.contains("\"orderId\" : 1"));
		assertTrue(json.contains("\"firstName\" : \"John\""));
		assertTrue(json.contains("\"shippingAddress\" : \"123 Test St\""));
		assertTrue(json.contains("\"totalPrice\" : 249.98"));
		assertTrue(json.contains("\"orderStatus\" : \"PENDING\""));
	}

	@Test
	void convertJsonToOrder_ShouldDeserializeAllFields() {
		// Given
		String json = orderMapperService.convertOrderToJson(testOrder);

		// When
		Order result = orderMapperService.convertJsonToOrder(json);

		// Then
		assertEquals(testOrder.getOrderId(), result.getOrderId());
		assertEquals(testOrder.getCustomer().getFirstName(), result.getCustomer().getFirstName());
		assertEquals(testOrder.getShippingAddress(), result.getShippingAddress());
		assertEquals(testOrder.getTotalPrice(), result.getTotalPrice());
		assertEquals(testOrder.getOrderStatus(), result.getOrderStatus());
		assertEquals(2, result.getProducts().size());
	}

	@Test
	void convertOrderListToJson_ShouldSerializeAllOrders() {
		// When
		String json = orderMapperService.convertOrderListToJson(testOrders);

		// Then
		assertTrue(json.contains("\"orderId\" : 1"));
		assertTrue(json.contains("\"orderId\" : 2"));
		assertTrue(json.contains("\"shippingAddress\" : \"123 Test St\""));
		assertTrue(json.contains("\"shippingAddress\" : \"456 Test Ave\""));
		assertTrue(json.contains("\"orderStatus\" : \"PENDING\""));
		assertTrue(json.contains("\"orderStatus\" : \"CONFIRMED\""));
	}

	@Test
	void convertJsonToOrderList_ShouldDeserializeAllOrders() {
		// Given
		String json = orderMapperService.convertOrderListToJson(testOrders);

		// When
		List<Order> result = orderMapperService.convertJsonToOrderList(json);

		// Then
		assertEquals(2, result.size());
		assertEquals(testOrders.get(0).getOrderId(), result.get(0).getOrderId());
		assertEquals(testOrders.get(0).getTotalPrice(), result.get(0).getTotalPrice());
		assertEquals(testOrders.get(1).getOrderId(), result.get(1).getOrderId());
		assertEquals(testOrders.get(1).getTotalPrice(), result.get(1).getTotalPrice());
	}

	@Test
	void updateOrderFromJson_ShouldUpdateOnlySpecifiedFields() {
		// Given
		String jsonPatch = "{\"shippingAddress\":\"789 New St\",\"orderStatus\":\"SHIPPED\"}";

		// When
		Order result = orderMapperService.updateOrderFromJson(testOrder, jsonPatch);

		// Then
		assertEquals("789 New St", result.getShippingAddress());
		assertEquals(OrderStatus.SHIPPED, result.getOrderStatus());
		assertEquals(testOrder.getCustomer(), result.getCustomer());
		assertEquals(testOrder.getTotalPrice(), result.getTotalPrice());
		assertEquals(testOrder.getProducts().size(), result.getProducts().size());
	}

	@Test
	void convertOrderToMap_ShouldContainAllFields() {
		// When
		Map<String, Object> result = orderMapperService.convertOrderToMap(testOrder);

		// Then
		assertEquals(1L, result.get("orderId"));
		assertEquals("123 Test St", result.get("shippingAddress"));
		assertEquals("PENDING", result.get("orderStatus").toString());
		assertNotNull(result.get("customer"));
		assertNotNull(result.get("products"));
	}

	@Test
	void convertMapToOrder_ShouldRestoreAllFields() {
		// Given
		Map<String, Object> map = orderMapperService.convertOrderToMap(testOrder);

		// When
		Order result = orderMapperService.convertMapToOrder(map);

		// Then
		assertEquals(testOrder.getOrderId(), result.getOrderId());
		assertEquals(testOrder.getShippingAddress(), result.getShippingAddress());
		assertEquals(testOrder.getOrderStatus(), result.getOrderStatus());
		assertEquals(testOrder.getTotalPrice(), result.getTotalPrice());
	}

	@Test
	void convertJsonToOrder_WithInvalidJson_ShouldThrowException() {
		// Given
		String invalidJson = "{invalid:json}";

		// Then
		assertThrows(RuntimeException.class, () ->
				orderMapperService.convertJsonToOrder(invalidJson)
		);
	}

	@Test
	void updateOrderFromJson_WithInvalidJson_ShouldThrowException() {
		// Given
		String invalidJson = "{invalid:json}";

		// Then
		assertThrows(RuntimeException.class, () ->
				orderMapperService.updateOrderFromJson(testOrder, invalidJson)
		);
	}

	@Test
	void updateOrderFromJson_WithComplexNestedUpdate_ShouldUpdateCorrectly() {
		// Given
		String jsonPatch = """
            {
                "shippingAddress": "789 New St",
                "orderStatus": "SHIPPED",
                "customer": {
                    "contactNumber": "9876543210"
                }
            }
            """;

		// When
		Order result = orderMapperService.updateOrderFromJson(testOrder, jsonPatch);

		// Then
		assertEquals("789 New St", result.getShippingAddress());
		assertEquals(OrderStatus.SHIPPED, result.getOrderStatus());
		assertEquals("9876543210", result.getCustomer().getContactNumber());
		// Verify other fields remain unchanged

		assertEquals(testOrder.getCustomer().getFirstName(), result.getCustomer().getFirstName());
		assertEquals(testOrder.getTotalPrice(), result.getTotalPrice());
	}

	@Test
	void convertOrderToJson_WithNullFields_ShouldSerializeSuccessfully() {
		// Given
		testOrder.setProducts(null);
		testOrder.setShippingAddress(null);

		// When
		String json = orderMapperService.convertOrderToJson(testOrder);
		Order result = orderMapperService.convertJsonToOrder(json);

		// Then
		assertNotNull(json);
		assertNull(result.getProducts());
		assertNull(result.getShippingAddress());
		assertNotNull(result.getCustomer());
	}
}