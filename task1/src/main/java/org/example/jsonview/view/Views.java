package org.example.jsonview.view;

public class Views {
	// Basic user information for list view
	public interface UserSummary {}

	// Detailed user information including orders
	public interface UserDetails extends UserSummary {}
}
