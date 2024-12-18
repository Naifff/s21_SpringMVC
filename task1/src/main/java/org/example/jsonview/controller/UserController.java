package org.example.jsonview.controller;

import org.example.jsonview.model.User;
import org.example.jsonview.service.UserService;
import org.example.jsonview.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	@JsonView(Views.UserSummary.class)
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/{id}")
	@JsonView(Views.UserDetails.class)
	public User getUserById(@PathVariable Long id) {
		return userService.getUserById(id);
	}

	@PostMapping
	@JsonView(Views.UserDetails.class)
	public User createUser(@Valid @RequestBody User user) {
		return userService.createUser(user);
	}

	@PutMapping("/{id}")
	@JsonView(Views.UserDetails.class)
	public User updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
		return userService.updateUser(id, user);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.ok().build();
	}
}