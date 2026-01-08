package services;

import Repository.UserRepository;
import interfaces.IdGenerator;
import interfaces.IUserService;
import models.User;
import models.RegularUser;
import models.AdminUser;

/**
 * UserService following SOLID principles:
 * - Single Responsibility: Manages user business logic only
 * - Dependency Inversion: Depends on UserRepository abstraction, not concrete implementation
 * - Open/Closed: Can be extended without modification
 */
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final IdGenerator userIdGenerator;
    private User currentUser;

    public UserService(UserRepository userRepository, IdGenerator userIdGenerator) {
        if (userRepository == null) throw new IllegalArgumentException("UserRepository cannot be null");
        if (userIdGenerator == null) throw new IllegalArgumentException("UserIdGenerator cannot be null");
        this.userRepository = userRepository;
        this.userIdGenerator = userIdGenerator;
        this.currentUser = null;
    }

    /**
     * Create a regular user with auto-generated ID
     */
    public User createRegularUser(String name, String email) {
        if (name == null || email == null) {
            throw new IllegalArgumentException("Name and email cannot be null");
        }

        RegularUser user = new RegularUser(name, email);
        // Set generated ID
        String generatedId = userIdGenerator.generate();
        user.setId(generatedId);

        int index = userIdGenerator.elementIndex(generatedId);
        userRepository.add(user, index);
        return user;
    }

    /**
     * Create an admin user with auto-generated ID
     */
    public User createAdminUser(String name, String email) {
        if (name == null || email == null) {
            throw new IllegalArgumentException("Name and email cannot be null");
        }

        AdminUser user = new AdminUser(name, email);
        // Set generated ID
        String generatedId = userIdGenerator.generate();
        user.setId(generatedId);

        int index = userIdGenerator.elementIndex(generatedId);
        userRepository.add(user, index);
        return user;
    }

    /**
     * Login user by ID
     */
    public User login(String userId) {
        if (userId == null) return null;

        User user = getUserById(userId);
        if (user != null) {
            currentUser = user;
        }
        return user;
    }

    /**
     * Logout current user
     */
    public void logout() {
        currentUser = null;
    }

    /**
     * Get current logged-in user
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Get all users
     */
    public User[] getAllUsers() {
        return userRepository.getAll();
    }

    /**
     * Get user by ID
     */
    public User getUserById(String userId) {
        if (userId == null) return null;
        return userRepository.findByUserId(userId);
    }

    /**
     * Get users by role
     */
    public User[] getUsersByRole(String role) {
        if (role == null) return new User[0];
        return userRepository.findByRole(role);
    }

    /**
     * Display current user information
     */
    public void displayCurrentUser() {
        if (currentUser != null) {
            currentUser.displayRole();
        } else {
            System.out.println("No user is currently logged in.");
        }
    }
}
