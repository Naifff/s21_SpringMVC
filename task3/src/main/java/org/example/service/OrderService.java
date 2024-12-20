package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Order;
import org.example.entity.OrderStatus;
import org.example.entity.Product;
import org.example.exception.BusinessException;
import org.example.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
	private final OrderRepository orderRepository;
	private final ProductService productService;

	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	public Order getOrderById(Long id) {
		return orderRepository.findById(id)
				.orElseThrow(() -> new BusinessException("Order not found with id: " + id));
	}

	@Transactional
	public Order createOrder(Order order) {
		// Validate and update product stock
		for (Product product : order.getProducts()) {
			Product currentProduct = productService.getProductById(product.getProductId());
			if (currentProduct.getQuantityInStock() < 1) {
				throw new BusinessException(
						"Product " + currentProduct.getName() + " is out of stock"
				);
			}
			currentProduct.setQuantityInStock(currentProduct.getQuantityInStock() - 1);
		}

		// Set initial order properties
		order.setOrderDate(LocalDateTime.now());
		order.setOrderStatus(OrderStatus.PENDING);

		return orderRepository.save(order);
	}

	@Transactional
	public Order updateOrder(Long id, Order updatedOrder) {
		// Find existing order or throw exception if not found
		Order existingOrder = orderRepository.findById(id)
				.orElseThrow(() -> new BusinessException("Order not found with id: " + id));

		// Validate customer exists
		if (updatedOrder.getCustomer() != null && !updatedOrder.getCustomer().equals(existingOrder.getCustomer())) {
			throw new BusinessException("Cannot change order customer");
		}

		// Update mutable fields while preserving immutable ones
		existingOrder.setShippingAddress(updatedOrder.getShippingAddress());

		// Update order status with validation
		if (updatedOrder.getOrderStatus() != null) {
			validateStatusTransition(existingOrder.getOrderStatus(), updatedOrder.getOrderStatus());
			existingOrder.setOrderStatus(updatedOrder.getOrderStatus());
		}

		// Handle product updates if provided
		if (updatedOrder.getProducts() != null && !updatedOrder.getProducts().isEmpty()) {
			// Validate product availability before updating
			for (Product product : updatedOrder.getProducts()) {
				Product currentProduct = productService.getProductById(product.getProductId());
				if (currentProduct.getQuantityInStock() < 1) {
					throw new BusinessException(
							"Product " + currentProduct.getName() + " is out of stock"
					);
				}
			}
			existingOrder.setProducts(updatedOrder.getProducts());

			// Update total price if products changed
			existingOrder.setTotalPrice(
					updatedOrder.getProducts().stream()
							.map(Product::getPrice)
							.reduce(BigDecimal.ZERO, BigDecimal::add)
			);
		}

		return orderRepository.save(existingOrder);
	}

	private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
		// Define valid status transitions
		boolean isValidTransition = switch (currentStatus) {
			case PENDING -> newStatus == OrderStatus.CONFIRMED || newStatus == OrderStatus.CANCELLED;
			case CONFIRMED -> newStatus == OrderStatus.SHIPPED || newStatus == OrderStatus.CANCELLED;
			case SHIPPED -> newStatus == OrderStatus.DELIVERED;
			case DELIVERED, CANCELLED -> false; // Final states - no further transitions allowed
		};

		if (!isValidTransition) {
			throw new BusinessException(
					String.format("Invalid status transition from %s to %s", currentStatus, newStatus)
			);
		}
	}
}