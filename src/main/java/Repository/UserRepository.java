package Repository;

import interfaces.IRepository;
import models.User;
import services.GenerateUserId;
import utils.exceptions.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * UserRepository following Single Responsibility Principle (SRP)
 * - Only responsible for User data persistence
 * - Implements IRepository for Dependency Inversion (DIP)
 */
public class UserRepository implements IRepository<User> {

    private List<User> users;
    private GenerateUserId userIdGenerator;

    public UserRepository() {
        this.users = new ArrayList<>();
        userIdGenerator = new GenerateUserId();
    }



    @Override
    public void add(User user, String userId) {
        if (user == null) throw new UserNotFoundException("User cannot be null");
        int idx = this.userIdGenerator.elementIndex(userId);
        if (idx < 0 ) throw new UserNotFoundException("Invalid index: " + userId);
        if (users.contains(user)) {
            throw new UserNotFoundException("User already exists at index " + userId);
        }
        users.add(idx, user);
    }


    @Override
    public User getById(String userId) {
        int idx = this.userIdGenerator.elementIndex(userId);
        if (idx < 0 || idx >= users.size()) throw new UserNotFoundException("Invalid index");
        return users.get(idx);
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public void update(String userId, User user) {
        int idx = this.userIdGenerator.elementIndex(userId);
        if (idx < 0) throw new UserNotFoundException("Invalid index");
        users.add(idx,user);
    }

    @Override
    public void removeById(String userId) {
        int idx = this.userIdGenerator.elementIndex(userId);
        if (idx < 0 || idx >= users.size()) return;
        users.remove(idx);
    }

    /**
     * Query helpers following Open/Closed Principle (OCP)
     */
    public User findByUserId(String userId) {
        if (userId == null) throw new UserNotFoundException("userId cannot be null");
        return users.stream()
                .filter(u -> u != null && userId.equals(u.getId()))
                .findFirst()
                .orElse(null);
    }

    public List<User> findByRole(String role) {
        if (role == null) throw new UserNotFoundException("No user with such role exist");
        return users.stream()
                .filter(u -> u != null && role.equalsIgnoreCase(u.getRole())).toList();
    }
}
