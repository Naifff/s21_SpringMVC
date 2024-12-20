package org.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfig {

	@Bean
	@Primary
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();

		// Register Java 8 time module for handling date/time types
		mapper.registerModule(new JavaTimeModule());

		// Configure date formatting
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		// Configure to handle lazy loading
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		// Configure to ignore unknown properties during deserialization
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		// Configure to fail on null values for primitives
		mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);

		// Additional configurations for better handling of our specific needs
		mapper.enable(SerializationFeature.INDENT_OUTPUT); // Pretty print JSON
		mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY); // More flexible array handling

		return mapper;
	}
}