package org.example.dto;

import lombok.Data;
import org.example.entity.OrderStatus;

import java.util.List;

@Data
public class OrderDTO {
	private Long customerId;
	private List<Long> productIds;
	private String shippingAddress;
	private OrderStatus orderStatus;
}