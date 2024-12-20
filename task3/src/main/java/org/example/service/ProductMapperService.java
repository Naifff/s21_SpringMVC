package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.example.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductMapperService {
	private final ObjectMapper objectMapper;

	// Example 1: Converting Product to JSON string with error handling
	public String convertProductToJson(Product product) {
		try {
			return objectMapper.writeValueAsString(product);
		} catch (Exception e) {
			throw new RuntimeException("Error converting product to JSON", e);
		}
	}

	// Example 2: Converting JSON string to Product object
	public Product convertJsonToProduct(String json) {
		try {
			return objectMapper.readValue(json, Product.class);
		} catch (Exception e) {
			throw new RuntimeException("Error converting JSON to product", e);
		}
	}

	// Example 3: Partially updating a Product using JSON patch
	public Product updateProductFromJson(Product product, String jsonPatch) {
		try {
			// Parse the patch JSON
			JsonNode patchNode = objectMapper.readTree(jsonPatch);

			// Convert product to JsonNode for modification
			JsonNode productNode = objectMapper.valueToTree(product);

			if (productNode instanceof ObjectNode) {
				ObjectNode productObjectNode = (ObjectNode) productNode;

				// Apply only the fields that are present in the patch
				patchNode.fields().forEachRemaining(field ->
						productObjectNode.set(field.getKey(), field.getValue())
				);

				// Convert back to Product
				return objectMapper.treeToValue(productObjectNode, Product.class);
			}

			return product;
		} catch (Exception e) {
			throw new RuntimeException("Error updating product from JSON", e);
		}
	}

	// Example 4: Converting a list of Products to JSON
	public String convertProductListToJson(List<Product> products) {
		try {
			return objectMapper.writeValueAsString(products);
		} catch (Exception e) {
			throw new RuntimeException("Error converting product list to JSON", e);
		}
	}

	// Example 5: Converting JSON to a list of Products
	public List<Product> convertJsonToProductList(String json) {
		try {
			return objectMapper.readValue(json, new TypeReference<List<Product>>() {
			});
		} catch (Exception e) {
			throw new RuntimeException("Error converting JSON to product list", e);
		}
	}

	// Example 6: Converting Product to Map
	public Map<String, Object> convertProductToMap(Product product) {
		return objectMapper.convertValue(product, Map.class);
	}

	// Example 7: Converting Map to Product
	public Product convertMapToProduct(Map<String, Object> map) {
		return objectMapper.convertValue(map, Product.class);
	}
}