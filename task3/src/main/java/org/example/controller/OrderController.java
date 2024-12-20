package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.entity.Order;
import org.example.service.OrderMapperService;
import org.example.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
	private final OrderMapperService orderMapper;

	private final OrderService orderService;

	@GetMapping
	public ResponseEntity<List<Order>> getAllOrders() {
		return ResponseEntity.ok(orderService.getAllOrders());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
		return ResponseEntity.ok(orderService.getOrderById(id));
	}

	@PostMapping
	public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order) {
		return new ResponseEntity<>(orderService.createOrder(order), HttpStatus.CREATED);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Order> updateOrder(
			@PathVariable Long id,
			@RequestBody String jsonPatch
	) {
		Order existingOrder = orderService.getOrderById(id);
		Order updatedOrder = orderMapper.updateOrderFromJson(existingOrder, jsonPatch);
		Order savedOrder = orderService.updateOrder(id, updatedOrder);
		return ResponseEntity.ok(savedOrder);
	}
}