package interfaces;

import models.User;

import java.util.List;

/**
 * UserService interface following Dependency Inversion Principle (DIP)
 * High-level modules should depend on abstractions, not concretions
 */
public interface IUserService {
    User createRegularUser(String name, String email);
    User createAdminUser(String name, String email);
    User login(String userId);
    void logout();
    User getCurrentUser();
    List<User> getAllUsers();
    User getUserById(String userId);
    List<User>getUsersByRole(String role);
    void displayCurrentUser();
}

