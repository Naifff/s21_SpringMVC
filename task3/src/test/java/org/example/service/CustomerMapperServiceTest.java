package org.example.service;

import org.example.config.JacksonConfig;
import org.example.config.ObjectMapperConfig;
import org.example.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JacksonConfig.class, OrderMapperService.class})
class CustomerMapperServiceTest {

	@Autowired
	private CustomerMapperService customerMapperService;

	private Customer testCustomer;
	private List<Customer> testCustomers;

	@BeforeEach
	void setUp() {
		testCustomer = new Customer();
		testCustomer.setCustomerId(1L);
		testCustomer.setFirstName("John");
		testCustomer.setLastName("Doe");
		testCustomer.setEmail("john.doe@example.com");
		testCustomer.setContactNumber("1234567890");

		Customer customer2 = new Customer();
		customer2.setCustomerId(2L);
		customer2.setFirstName("Jane");
		customer2.setLastName("Smith");
		customer2.setEmail("jane.smith@example.com");
		customer2.setContactNumber("0987654321");

		testCustomers = Arrays.asList(testCustomer, customer2);
	}

	@Test
	void convertCustomerToJson_ShouldSerializeAllFields() {
		// When
		String json = customerMapperService.convertCustomerToJson(testCustomer);

		// Then
		assertTrue(json.contains("\"customerId\":1"));
		assertTrue(json.contains("\"firstName\":\"John\""));
		assertTrue(json.contains("\"lastName\":\"Doe\""));
		assertTrue(json.contains("\"email\":\"john.doe@example.com\""));
	}

	@Test
	void convertJsonToCustomer_ShouldDeserializeAllFields() {
		// Given
		String json = customerMapperService.convertCustomerToJson(testCustomer);

		// When
		Customer result = customerMapperService.convertJsonToCustomer(json);

		// Then
		assertEquals(testCustomer.getCustomerId(), result.getCustomerId());
		assertEquals(testCustomer.getFirstName(), result.getFirstName());
		assertEquals(testCustomer.getLastName(), result.getLastName());
		assertEquals(testCustomer.getEmail(), result.getEmail());
	}

	@Test
	void convertCustomerListToJson_ShouldSerializeAllCustomers() {
		// When
		String json = customerMapperService.convertCustomerListToJson(testCustomers);

		// Then
		assertTrue(json.contains("\"firstName\":\"John\""));
		assertTrue(json.contains("\"firstName\":\"Jane\""));
		assertTrue(json.contains("\"email\":\"john.doe@example.com\""));
		assertTrue(json.contains("\"email\":\"jane.smith@example.com\""));
	}

	@Test
	void convertJsonToCustomerList_ShouldDeserializeAllCustomers() {
		// Given
		String json = customerMapperService.convertCustomerListToJson(testCustomers);

		// When
		List<Customer> result = customerMapperService.convertJsonToCustomerList(json);

		// Then
		assertEquals(2, result.size());
		assertEquals(testCustomers.get(0).getEmail(), result.get(0).getEmail());
		assertEquals(testCustomers.get(1).getEmail(), result.get(1).getEmail());
	}

	@Test
	void updateCustomerFromJson_ShouldUpdateOnlySpecifiedFields() {
		// Given
		String jsonPatch = "{\"firstName\":\"Johnny\",\"email\":\"johnny.doe@example.com\"}";

		// When
		Customer result = customerMapperService.updateCustomerFromJson(testCustomer, jsonPatch);

		// Then
		assertEquals("Johnny", result.getFirstName());
		assertEquals("johnny.doe@example.com", result.getEmail());
		assertEquals(testCustomer.getLastName(), result.getLastName());
		assertEquals(testCustomer.getContactNumber(), result.getContactNumber());
	}

	@Test
	void convertCustomerToMap_ShouldContainAllFields() {
		// When
		Map<String, Object> result = customerMapperService.convertCustomerToMap(testCustomer);

		// Then
		assertEquals(1L, result.get("customerId"));
		assertEquals("John", result.get("firstName"));
		assertEquals("Doe", result.get("lastName"));
		assertEquals("john.doe@example.com", result.get("email"));
	}

	@Test
	void convertMapToCustomer_ShouldRestoreAllFields() {
		// Given
		Map<String, Object> map = customerMapperService.convertCustomerToMap(testCustomer);

		// When
		Customer result = customerMapperService.convertMapToCustomer(map);

		// Then
		assertEquals(testCustomer.getCustomerId(), result.getCustomerId());
		assertEquals(testCustomer.getFirstName(), result.getFirstName());
		assertEquals(testCustomer.getEmail(), result.getEmail());
	}

	@Test
	void convertJsonToCustomer_WithInvalidJson_ShouldThrowException() {
		// Given
		String invalidJson = "{invalid:json}";

		// Then
		assertThrows(RuntimeException.class, () ->
				customerMapperService.convertJsonToCustomer(invalidJson)
		);
	}

	@Test
	void updateCustomerFromJson_WithInvalidJson_ShouldThrowException() {
		// Given
		String invalidJson = "{invalid:json}";

		// Then
		assertThrows(RuntimeException.class, () ->
				customerMapperService.updateCustomerFromJson(testCustomer, invalidJson)
		);
	}
}