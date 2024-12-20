package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "orderId"
)
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	@NotNull(message = "Customer is required")
	@JsonManagedReference
	private Customer customer;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "order_products",
			joinColumns = @JoinColumn(name = "order_id"),
			inverseJoinColumns = @JoinColumn(name = "product_id")
	)
	private List<Product> products;

	@NotNull(message = "Order date is required")
	private LocalDateTime orderDate;

	@NotBlank(message = "Shipping address is required")
	private String shippingAddress;

	@NotNull(message = "Total price is required")
	private BigDecimal totalPrice;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
}