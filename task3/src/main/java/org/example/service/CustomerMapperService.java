package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.example.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomerMapperService {
	private final ObjectMapper objectMapper;

	// Convert a single Customer to JSON string
	public String convertCustomerToJson(Customer customer) {
		try {
			return objectMapper.writeValueAsString(customer);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error converting customer to JSON", e);
		}
	}

	// Convert JSON string to Customer object
	public Customer convertJsonToCustomer(String json) {
		try {
			return objectMapper.readValue(json, Customer.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error converting JSON to customer", e);
		}
	}

	// Convert a list of Customers to JSON string
	public String convertCustomerListToJson(List<Customer> customers) {
		try {
			return objectMapper.writeValueAsString(customers);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error converting customer list to JSON", e);
		}
	}

	// Convert JSON string to a list of Customers
	public List<Customer> convertJsonToCustomerList(String json) {
		try {
			return objectMapper.readValue(json, new TypeReference<List<Customer>>() {
			});
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error converting JSON to customer list", e);
		}
	}

	// Partially update a Customer using JSON patch
	public Customer updateCustomerFromJson(Customer customer, String jsonPatch) {
		try {
			// Parse the patch JSON
			JsonNode patchNode = objectMapper.readTree(jsonPatch);

			// Convert customer to JsonNode for modification
			JsonNode customerNode = objectMapper.valueToTree(customer);

			if (customerNode instanceof ObjectNode) {
				ObjectNode customerObjectNode = (ObjectNode) customerNode;

				// Apply only the fields that are present in the patch
				patchNode.fields().forEachRemaining(field ->
						customerObjectNode.set(field.getKey(), field.getValue())
				);

				// Convert back to Customer
				return objectMapper.treeToValue(customerObjectNode, Customer.class);
			}

			return customer;
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error updating customer from JSON", e);
		}
	}

	// Convert Customer to Map for flexible data manipulation
	public Map<String, Object> convertCustomerToMap(Customer customer) {
		return objectMapper.convertValue(customer, Map.class);
	}

	// Convert Map back to Customer
	public Customer convertMapToCustomer(Map<String, Object> map) {
		return objectMapper.convertValue(map, Customer.class);
	}
}