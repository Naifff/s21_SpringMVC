package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.example.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderMapperService {
	private final ObjectMapper objectMapper;

	// Convert a single Order to JSON string, handling nested relationships
	public String convertOrderToJson(Order order) {
		try {
			return objectMapper.writeValueAsString(order);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error converting order to JSON", e);
		}
	}

	// Convert JSON string to Order object, handling nested relationships
	public Order convertJsonToOrder(String json) {
		try {
			return objectMapper.readValue(json, Order.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error converting JSON to order", e);
		}
	}

	// Convert a list of Orders to JSON string
	public String convertOrderListToJson(List<Order> orders) {
		try {
			return objectMapper.writeValueAsString(orders);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error converting order list to JSON", e);
		}
	}

	// Convert JSON string to a list of Orders
	public List<Order> convertJsonToOrderList(String json) {
		try {
			return objectMapper.readValue(json, new TypeReference<List<Order>>() {});
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error converting JSON to order list", e);
		}
	}

	// Partially update an Order using JSON patch
	public Order updateOrderFromJson(Order order, String jsonPatch) {
		try {
			// Parse the patch JSON
			JsonNode patchNode = objectMapper.readTree(jsonPatch);

			// Convert order to JsonNode for modification
			JsonNode orderNode = objectMapper.valueToTree(order);

			if (orderNode instanceof ObjectNode) {
				ObjectNode orderObjectNode = (ObjectNode) orderNode;

				// Apply only the fields that are present in the patch
				patchNode.fields().forEachRemaining(field ->
						orderObjectNode.set(field.getKey(), field.getValue())
				);

				// Convert back to Order
				return objectMapper.treeToValue(orderObjectNode, Order.class);
			}

			return order;
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error updating order from JSON", e);
		}
	}

	// Convert Order to Map for flexible data manipulation
	public Map<String, Object> convertOrderToMap(Order order) {
		return objectMapper.convertValue(order, Map.class);
	}

	// Convert Map back to Order
	public Order convertMapToOrder(Map<String, Object> map) {
		return objectMapper.convertValue(map, Order.class);
	}
}