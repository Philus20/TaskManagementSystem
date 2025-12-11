package Repository;

import interfaces.IRepository;
import models.User;

import java.util.Arrays;

/**
 * UserRepository following Single Responsibility Principle (SRP)
 * - Only responsible for User data persistence
 * - Implements IRepository for Dependency Inversion (DIP)
 */
public class UserRepository implements IRepository<User> {

    private User[] users;

    public UserRepository(int initialCapacity) {
        if (initialCapacity <= 0) initialCapacity = 20;
        this.users = new User[initialCapacity];
    }

    private void ensureCapacity(int index) {
        if (index < users.length) return;

        int newCapacity = Math.max(users.length * 2, 1);
        while (newCapacity <= index) newCapacity *= 2;
        users = Arrays.copyOf(users, newCapacity);
    }

    @Override
    public void add(User user, int index) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        ensureCapacity(index);
        if (users[index] != null)
            throw new IllegalStateException("User already exists at index " + index);

        users[index] = user;
    }

    @Override
    public User getById(int index) {
        if (index < 0 || index >= users.length) return null;
        return users[index];
    }

    @Override
    public User[] getAll() {
        // Return trimmed array (no null slots)
        int count = 0;
        for (User u : users) if (u != null) count++;
        User[] result = new User[count];
        int i = 0;
        for (User u : users) if (u != null) result[i++] = u;
        return result;
    }

    @Override
    public void update(int index, User user) {
        if (index < 0) throw new IllegalArgumentException("Invalid index");
        ensureCapacity(index);
        users[index] = user;
    }

    @Override
    public void removeById(int index) {
        if (index < 0 || index >= users.length) return;
        users[index] = null;
    }

    /**
     * Query helpers following Open/Closed Principle (OCP)
     */
    public User findByUserId(String userId) {
        if (userId == null) return null;
        return Arrays.stream(users)
                .filter(u -> u != null && userId.equals(u.getId()))
                .findFirst()
                .orElse(null);
    }

    public User[] findByRole(String role) {
        if (role == null) return new User[0];
        return Arrays.stream(users)
                .filter(u -> u != null && role.equalsIgnoreCase(u.getRole()))
                .toArray(User[]::new);
    }
}
