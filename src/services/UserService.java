package services;

import models.User;
import models.RegularUser;
import models.AdminUser;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private List<User> users;
    private User currentUser;

    public UserService() {
        this.users = new ArrayList<>();
        this.currentUser = null;
    }

    public User createRegularUser(String name, String email) {
        RegularUser user = new RegularUser(name, email);
        users.add(user);
        return user;
    }

    public User createAdminUser(String name, String email) {
        AdminUser user = new AdminUser(name, email);
        users.add(user);
        return user;
    }

    public User login(String userId) {
        User user = users.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElse(null);

        if (user != null) {
            currentUser = user;
        }

        return user;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public User getUserById(String userId) {
        return users.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    public void displayCurrentUser() {
        if (currentUser != null) {
            currentUser.displayRole();
        } else {
            System.out.println("No user is currently logged in.");
        }
    }
}
