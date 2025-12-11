package services;

import interfaces.IUserService;
import models.User;

/**
 * PermissionService following Single Responsibility Principle (SRP)
 * - Only responsible for authorization and permission checking
 */
public class PermissionService {
    private final IUserService userService;

    public PermissionService(IUserService userService) {
        if (userService == null) throw new IllegalArgumentException("UserService cannot be null");
        this.userService = userService;
    }

    /**
     * Check if current user is admin
     */
    public boolean isAdmin() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) return false;
        return "Admin User".equals(currentUser.getRole());
    }

    /**
     * Check if user is logged in
     */
    public boolean isLoggedIn() {
        return userService.getCurrentUser() != null;
    }

    /**
     * Check if user has permission to perform admin actions
     * @return true if admin, false otherwise
     */
    public boolean checkAdminPermission(String action) {
        if (!isAdmin()) {
            String role = isLoggedIn() ? userService.getCurrentUser().getRole() : "Not logged in";
            System.out.println("Access Denied: Only Admin users can " + action + ".");
            System.out.println("Your current role: " + role);
            return false;
        }
        return true;
    }

    /**
     * Check if user is logged in for actions requiring authentication
     * @return true if logged in, false otherwise
     */
    public boolean checkLoggedInPermission(String action) {
        if (!isLoggedIn()) {
            System.out.println("Access Denied: You must be logged in to " + action + ".");
            System.out.println("Please login or sign up first.");
            System.out.println();
            return false;
        }
        return true;
    }

    /**
     * Get current user role
     */
    public String getCurrentUserRole() {
        User currentUser = userService.getCurrentUser();
        return currentUser != null ? currentUser.getRole() : "Guest";
    }
}

