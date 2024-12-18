package org.example.jsonview.service;

import org.example.jsonview.exception.ResourceNotFoundException;
import org.example.jsonview.model.User;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {
	private final Map<Long, User> userStore = new ConcurrentHashMap<>();
	private final AtomicLong idGenerator = new AtomicLong(1);

	public List<User> getAllUsers() {
		return new ArrayList<>(userStore.values());
	}

	public User getUserById(Long id) {
		User user = userStore.get(id);
		if (user == null) {
			throw new ResourceNotFoundException("User not found with id: " + id);
		}
		return user;
	}

	public User createUser(User user) {
		user.setId(idGenerator.getAndIncrement());
		userStore.put(user.getId(), user);
		return user;
	}

	public User updateUser(Long id, User userDetails) {
		User user = getUserById(id);
		user.setName(userDetails.getName());
		user.setEmail(userDetails.getEmail());
		userStore.put(id, user);
		return user;
	}

	public void deleteUser(Long id) {
		if (userStore.remove(id) == null) {
			throw new ResourceNotFoundException("User not found with id: " + id);
		}
	}
}