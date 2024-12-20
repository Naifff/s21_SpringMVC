package org.example.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.entity.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JsonUtils {
	// Create an ObjectMapper instance with custom configuration
	private final ObjectMapper objectMapper;

	public JsonUtils() {
		this.objectMapper = new ObjectMapper();
		// Register module to handle Java 8 date/time types
		objectMapper.registerModule(new JavaTimeModule());
		// Configure to write dates as timestamps
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		// Enable pretty printing for better readability
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
	}

	// Convert a single object to JSON string
	public String toJson(Object object) throws JsonProcessingException {
		return objectMapper.writeValueAsString(object);
	}

	// Convert JSON string back to specific object type
	public <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException {
		return objectMapper.readValue(json, clazz);
	}

	// Convert JSON string to List of objects
	public <T> List<T> fromJsonList(String json, TypeReference<List<T>> typeReference)
			throws JsonProcessingException {
		return objectMapper.readValue(json, typeReference);
	}

	// Example usage with our Product entity
	public String demonstrateJsonConversion() throws JsonProcessingException {
		// Create a sample product
		Product product = new Product();
		product.setName("Example Product");
		product.setPrice(new java.math.BigDecimal("29.99"));
		product.setDescription("This is an example product");
		product.setQuantityInStock(100);

		// Convert product to JSON
		String productJson = toJson(product);
		System.out.println("Product as JSON:");
		System.out.println(productJson);

		// Convert JSON back to Product object
		Product reconstructedProduct = fromJson(productJson, Product.class);
		System.out.println("\nReconstructed product name: " + reconstructedProduct.getName());

		return productJson;
	}
}