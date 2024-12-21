package org.example.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "productId"
)
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;

	@NotBlank(message = "Product name is required")
	private String name;

	@Column(length = 1000)
	private String description;

	@NotNull(message = "Price is required")
	@Positive(message = "Price must be positive")
	private BigDecimal price;

	@NotNull(message = "Quantity is required")
	@Positive(message = "Quantity must be positive")
	private Integer quantityInStock;
}