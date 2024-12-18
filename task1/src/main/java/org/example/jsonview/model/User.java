package org.example.jsonview.model;

import org.example.jsonview.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

public class User {
	@JsonView(Views.UserSummary.class)
	private Long id;

	@JsonView(Views.UserSummary.class)
	@NotBlank(message = "Name is required")
	private String name;

	@JsonView(Views.UserSummary.class)
	@Email(message = "Invalid email format")
	@NotBlank(message = "Email is required")
	private String email;

	@JsonView(Views.UserDetails.class)
	private List<Order> orders = new ArrayList<>();

	// Getters and setters
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public List<Order> getOrders() { return orders; }
	public void setOrders(List<Order> orders) { this.orders = orders; }
}