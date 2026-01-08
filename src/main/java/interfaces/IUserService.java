package interfaces;

import models.User;

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
    User[] getAllUsers();
    User getUserById(String userId);
    User[] getUsersByRole(String role);
    void displayCurrentUser();
}

