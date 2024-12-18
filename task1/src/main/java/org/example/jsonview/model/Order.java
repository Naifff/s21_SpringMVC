package org.example.jsonview.model;

import org.example.jsonview.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import java.math.BigDecimal;

public class Order {
	@JsonView(Views.UserDetails.class)
	private Long id;

	@JsonView(Views.UserDetails.class)
	private String items;

	@JsonView(Views.UserDetails.class)
	private BigDecimal totalAmount;

	@JsonView(Views.UserDetails.class)
	private String status;

	// Getters and setters
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getItems() { return items; }
	public void setItems(String items) { this.items = items; }
	public BigDecimal getTotalAmount() { return totalAmount; }
	public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
}