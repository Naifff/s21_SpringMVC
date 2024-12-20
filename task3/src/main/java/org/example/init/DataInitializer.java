package org.example.init;

import lombok.RequiredArgsConstructor;
import org.example.entity.Customer;
import org.example.entity.Product;
import org.example.repository.CustomerRepository;
import org.example.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
	private final ProductRepository productRepository;
	private final CustomerRepository customerRepository;

	@Override
	public void run(String... args) {
		// Initialize products
		Product product1 = new Product(null, "Laptop", "High-end laptop", new BigDecimal("999.99"), 10);
		Product product2 = new Product(null, "Smartphone", "Latest model", new BigDecimal("599.99"), 15);
		Product product3 = new Product(null, "Headphones", "Wireless headphones", new BigDecimal("99.99"), 20);

		productRepository.saveAll(Arrays.asList(product1, product2, product3));

		// Initialize customers
		Customer customer1 = new Customer(null, "John", "Doe", "john.doe@example.com", "1234567890");
		Customer customer2 = new Customer(null, "Jane", "Smith", "jane.smith@example.com", "0987654321");

		customerRepository.saveAll(Arrays.asList(customer1, customer2));
	}
}